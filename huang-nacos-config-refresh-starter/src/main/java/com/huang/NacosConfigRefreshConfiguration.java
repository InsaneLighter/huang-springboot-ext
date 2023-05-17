package com.huang;

import com.huang.ext.NacosConfigRefreshPostProcessor;
import com.huang.listener.CustomizedConfigChangeListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Time 2023-03-28 21:48
 * Created by Huang
 * className: NacosConfigRefreshConfiguration
 * Description:
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cloud.nacos.config", name = "refresh-enabled", havingValue = "true", matchIfMissing = false)
public class NacosConfigRefreshConfiguration {

    @Bean
    public NacosConfigRefreshPostProcessor configRefreshPostProcessor(){
        NacosConfigRefreshPostProcessor nacosConfigRefreshPostProcessor = new NacosConfigRefreshPostProcessor();
        System.out.println("" +
                "    //    / /                                           //   ) )                    //  ) )                  //   ) )           //  ) )                               \n" +
                "   //___ / /           ___       __      ___           //         ___       __   __//__  ( )  ___           //___/ /   ___   __//__  __      ___      ___     / __    \n" +
                "  / ___   / //   / / //   ) ) //   ) ) //   ) ) ____  //        //   ) ) //   ) ) //    / / //   ) ) ____  / ___ (   //___) ) //   //  ) ) //___) ) ((   ) ) //   ) ) \n" +
                " //    / / //   / / //   / / //   / / ((___/ /       //        //   / / //   / / //    / / ((___/ /       //   | |  //       //   //      //         \\ \\  //   / /  \n" +
                "//    / / ((___( ( ((___( ( //   / /   //__         ((____/ / ((___/ / //   / / //    / /   //__         //    | | ((____   //   //      ((____   //   ) ) //   / /   \n");
        return nacosConfigRefreshPostProcessor;
    }

    @Bean
    public CustomizedConfigChangeListener customizedConfigChangeListener(ApplicationContext applicationContext){
        return new CustomizedConfigChangeListener(applicationContext);
    }

}