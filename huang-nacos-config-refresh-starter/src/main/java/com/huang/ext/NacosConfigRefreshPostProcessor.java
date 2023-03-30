package com.huang.ext;

import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.PropertyChangeType;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor;
import com.huang.event.NacosConfigChangeEvent;
import com.huang.utils.NacosConfigParserUtils;
import com.huang.utils.PlaceholderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2023-03-28 21:50
 * Created by Huang
 * className: NacosConfigRefreshPostProcessor
 * Description: 监听nacos配置变更，更改@Value、@NacosValue注入值
 */
@Slf4j
public class NacosConfigRefreshPostProcessor extends AbstractAnnotationBeanPostProcessor implements EnvironmentAware, ApplicationListener<NacosConfigChangeEvent> {
    /**
     * nacos对应的propertySource的类名称
     */
    private static final String NACOS_PROPERTY_SOURCE_CLASS_NAME = "com.alibaba.nacos.spring.core.env.NacosPropertySource";
    /**
     * 存放被@Value和@NacosValue修饰的属性 key = 占位符 value = 属性集合
     */
    private final static Map<String, List<FieldInstance>> PLACEHOLDER_VALUE_TARGET_MAP = new HashMap<>();

    /**
     * 当前Nacos环境配置，第一次从环境对象中获取，后续变更后会被新的属性覆盖
     */
    private Map<String, Object> currentPlaceholderConfigMap = new ConcurrentHashMap<>();
    /**
     * spring环境对象
     */
    private StandardEnvironment standardEnvironment;
    /**
     * 类型转换服务
     */
    private final ConversionService conversionService = new DefaultConversionService();

    public NacosConfigRefreshPostProcessor() {
        super(Value.class, NacosValue.class);
    }


    @Override
    public void onApplicationEvent(NacosConfigChangeEvent event) {
        // 将配置内容解析成键值对
        Map<String, Object> newConfigMap;
        try {
            ConfigData configData = (ConfigData) event.getSource();
            newConfigMap = parseConfigContext(configData);
            // 刷新变更对象的值
            refreshTargetObjectFieldValue(newConfigMap);
            // 当前配置指向最新的配置
            currentPlaceholderConfigMap = newConfigMap;
        } catch (Exception e) {
            log.error("nacos配置内容解析异常: {}", e.toString());
        }
    }

    @Override
    protected Object doGetInjectedBean(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) throws Exception {
        // 注解占位符内容
        String placeholderKey = (String) attributes.get("value");
        // 解析嵌套占位符（只剩下最外层占位符）
        placeholderKey = PlaceholderUtils.parseStringValue(placeholderKey, standardEnvironment, null);
        // 剔除默认值后的占位符
        String key = PlaceholderUtils.getPlaceholderKey(placeholderKey);
        // 默认值
        String defaultValue = PlaceholderUtils.getPlaceholderDefaultValue(placeholderKey);

        Field field = (Field) injectedElement.getMember();
        // 属性记录到缓存中
        addFieldInstance(key, field, bean);

        // 环境对象中当前属性
        String value = standardEnvironment.getProperty(key);
        // 环境对象不存在该值，就取默认值
        if (Objects.isNull(value)) {
            value = defaultValue;
        }
        // 将字符串类型转换为目标属性类型
        return conversionService.convert(value, field.getType());
    }

    @Override
    protected String buildInjectedObjectCacheKey(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) {
        return bean.getClass().getName() + "#" + injectedElement.getMember().getName();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.standardEnvironment = (StandardEnvironment) environment;
        for (PropertySource<?> propertySource : standardEnvironment.getPropertySources()) {
            // 筛选出nacos的配置
            if (propertySource.getClass().getName().equals(NACOS_PROPERTY_SOURCE_CLASS_NAME)) {
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                // 配置以键值对形式存储到当前属性配置集合中
                for (String propertyName : mapPropertySource.getPropertyNames()) {
                    currentPlaceholderConfigMap.put(propertyName, mapPropertySource.getProperty(propertyName));
                }
            }
        }
    }

    /**
     * 刷新变更对象的值
     *
     * @param newConfigMap
     */
    private void refreshTargetObjectFieldValue(Map<String, Object> newConfigMap) {
        if (newConfigMap == null) {
            return;
        }
        // 对比两次配置内容，筛选出变更后的配置项
        Map<String, ConfigChangeItem> configChangeItemMap = NacosConfigParserUtils.filterChangeData(currentPlaceholderConfigMap, newConfigMap);

        // 反射给对象赋值
        for (String key : configChangeItemMap.keySet()) {
            // 没有用到的配置 直接跳过
            if (!PLACEHOLDER_VALUE_TARGET_MAP.containsKey(key)) {
                continue;
            }
            ConfigChangeItem item = configChangeItemMap.get(key);
            // 删除配置不改变对象的值
            if (PropertyChangeType.DELETED.equals(item.getType())) {
                log.info("Nacos-config-refresh-starter: deleted property [" + item.getKey() + "]");
                continue;
            }
            // 嵌套占位符 防止中途嵌套中的配置变了 导致对象属性刷新失败
            if (PLACEHOLDER_VALUE_TARGET_MAP.containsKey(item.getOldValue())) {
                List<FieldInstance> fieldInstances = PLACEHOLDER_VALUE_TARGET_MAP.get(item.getOldValue());
                PLACEHOLDER_VALUE_TARGET_MAP.put(item.getNewValue(), fieldInstances);
                PLACEHOLDER_VALUE_TARGET_MAP.remove(item.getOldValue());
            }
            updateFieldValue(key, item.getNewValue(), item.getOldValue());
        }
    }

    /**
     * 解析nacos的配置
     *
     * @param configData
     * @return
     * @throws Exception
     */
    private Map<String, Object> parseConfigContext(ConfigData configData) throws Exception {
        Map<String, Object> newConfigMap;
        String fileExtension = NacosDataParserHandler.getInstance().getFileExtension(configData.getDataId());
        Boolean validType = ConfigType.isValidType(fileExtension);
        if (validType) {
            newConfigMap = convertContentToMap(fileExtension,configData.getContent());
        } else {
            throw new Exception(String.format(
                    "配置文件类型解析异常, dataId=[%s], group=[%s]",
                    configData.getDataId(),
                    configData.getGroup()));
        }
        // 筛选出正确的配置
        return NacosConfigParserUtils.getFlattenedMap(newConfigMap);
    }

    private Map<String, Object> convertContentToMap(String fileExtension, String content) throws Exception {
        if(StringUtils.isEmpty(fileExtension)){
            throw new Exception("配置文件fileExtension为空");
        }
        Map<String, Object> newConfigMap = new HashMap<>();
        if (ConfigType.YAML.getType().equals(fileExtension)) {
            newConfigMap = (new Yaml()).load(content);
        } else if (ConfigType.PROPERTIES.getType().equals(fileExtension)) {
            Properties newProps = new Properties();
            newProps.load(new StringReader(content));
            newConfigMap = new HashMap<>((Map) newProps);
        }
        return newConfigMap;
    }


    private static class FieldInstance {
        final Object bean;

        final Field field;

        public FieldInstance(Object bean, Field field) {
            this.bean = bean;
            this.field = field;
        }
    }

    /**
     * 将被@Value和@NacosValue修饰的属性，以键值对的形式存放到当前属性配置集合中
     *
     * @param key
     * @param field
     * @param bean
     */
    private void addFieldInstance(String key, Field field, Object bean) {
        List<FieldInstance> fieldInstances = PLACEHOLDER_VALUE_TARGET_MAP.get(key);
        if (CollectionUtils.isEmpty(fieldInstances)) {
            fieldInstances = new ArrayList<>();
        }
        fieldInstances.add(new FieldInstance(bean, field));
        PLACEHOLDER_VALUE_TARGET_MAP.put(key, fieldInstances);
    }

    /**
     * 反射修改变更的对象属性值
     */
    private void updateFieldValue(String key, String newValue, String oldValue) {
        List<FieldInstance> fieldInstances = PLACEHOLDER_VALUE_TARGET_MAP.get(key);
        for (FieldInstance instance : fieldInstances) {
            try {
                ReflectionUtils.makeAccessible(instance.field);
                // 类型转换
                Object value = conversionService.convert(newValue, instance.field.getType());
                instance.field.set(instance.bean, value);
            } catch (Throwable e) {
                log.warn("Can't update value of the " + instance.field.getName() + " (field) in " + instance.bean.getClass().getSimpleName() + " (bean)");
            }
            log.info("Nacos-config-refresh-starter: " + instance.bean.getClass().getSimpleName() + "#" + instance.field.getName() + " field value changed from [" + oldValue + "] to [" + newValue + "]");
        }
    }
}