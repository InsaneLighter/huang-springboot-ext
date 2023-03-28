package com.huang.listener;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.alibaba.cloud.nacos.refresh.NacosContextRefresher;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.huang.event.NacosConfigChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Time 2023-03-28 21:52
 * Created by Huang
 * className: ConfigChangeListener
 * Description: 配置变更监听器
 */
@Slf4j
@Component
public class ConfigChangeListener extends AbstractConfigChangeListener {
    ConfigService configService;

    @Autowired
    NacosContextRefresher nacosContextRefresher;

    @Autowired
    NacosConfigProperties nacosConfigProperties;

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() throws NacosException {
        //手动获取configService
        configService = NacosFactory.createConfigService(nacosConfigProperties.assembleConfigServiceProperties());
        //手动注册监听器（同nacos需要打开refresh属性）
        for (NacosPropertySource propertySource : NacosPropertySourceRepository.getAll()) {
            if (!propertySource.isRefreshable()) {
                continue;
            }
            String dataId = propertySource.getDataId();
            String group = propertySource.getGroup();
            registerListener(group, dataId);
        }
    }

    /**
     * 监听配置变更并发布事件
     */
    @Override
    public void receiveConfigInfo(String configInfo) {
        NacosConfigChangeEvent configUpdateEvent = new NacosConfigChangeEvent(configInfo);
        applicationContext.publishEvent(configUpdateEvent);
    }

    @Override
    public void receiveConfigChange(ConfigChangeEvent configChangeEvent) {
        //ignore
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