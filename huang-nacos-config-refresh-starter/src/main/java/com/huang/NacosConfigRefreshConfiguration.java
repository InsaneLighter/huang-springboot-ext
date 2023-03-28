package com.huang;

import com.huang.ext.NacosConfigRefreshPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Time 2023-03-28 21:48
 * Created by Huang
 * className: NacosConfigRefreshConfiguration
 * Description:
 */
@Configuration
public class NacosConfigRefreshConfiguration {
    @Bean
    public NacosConfigRefreshPostProcessor configRefreshPostProcessor(){
        return new NacosConfigRefreshPostProcessor();
    }
}