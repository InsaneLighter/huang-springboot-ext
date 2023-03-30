package com.huang.listener;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractSharedListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.huang.event.NacosConfigChangeEvent;
import com.huang.ext.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @Time 2023-03-28 21:52
 * Created by Huang
 * className: ConfigChangeListener
 * Description: 配置变更监听器
 * TODO 更换监听器AbstractSharedListener
 */
@Slf4j
public class CustomizedConfigChangeListener extends AbstractSharedListener {

    private ConfigService configService;

    private final ApplicationContext applicationContext;

    public CustomizedConfigChangeListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() throws NacosException {
        log.info("ConfigChangeListener init starting");
        //手动获取configService
        NacosConfigProperties nacosConfigProperties = applicationContext.getBean(NacosConfigProperties.class);
        configService = NacosFactory.createConfigService(nacosConfigProperties.assembleConfigServiceProperties());
        //手动注册监听器（同nacos需要打开refresh属性）
        for (NacosPropertySource propertySource : NacosPropertySourceRepository.getAll()) {
            if (!propertySource.isRefreshable()) {
                continue;
            }
            String dataId = propertySource.getDataId();
            String group = propertySource.getGroup();
            registerListener(group, dataId);
            log.info("ConfigChangeListener registerListener : group({}) dataId({})", group, dataId);
        }
        log.info("ConfigChangeListener init end");
    }

    /**
     * 监听配置变更并发布事件
     */
    @Override
    public void innerReceive(String dataId, String group, String configInfo) {
        log.info("ConfigChangeListener config update : dataId-{} group-{} config-[{}]", dataId, group, configInfo);
        ConfigData configData = new ConfigData(dataId, group, configInfo);
        NacosConfigChangeEvent configUpdateEvent = new NacosConfigChangeEvent(configData);
        applicationContext.publishEvent(configUpdateEvent);
    }

    /**
     * 手动注册配置变更监听器
     */
    private void registerListener(final String groupKey, final String dataKey) {
        try {
            configService.addListener(dataKey, groupKey, this);
        } catch (NacosException e) {
            log.warn(String.format(
                    "register fail for nacos listener ,dataId=[%s],group=[%s]", dataKey,
                    groupKey), e);
        }
    }
}