package com.huang.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Time 2023-03-28 21:51
 * Created by Huang
 * className: NacosConfigChangeEvent
 * Description: 配置变更事件
 */
public class NacosConfigChangeEvent extends ApplicationEvent {
    public NacosConfigChangeEvent(Object source) {
        super(source);
    }
}