package com.huang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @Date 2023-05-30
 * Created by Huang
 * className: SwaggerPrinter
 * Description:
 */
public class SwaggerPrinter implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(SwaggerPrinter.class);

    private final Environment environment;

    public SwaggerPrinter(ApplicationContext applicationContext) {
        this.environment = applicationContext.getEnvironment();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();
        logger.info("当前环境：{}", activeProfiles.length == 0 ? "default" : activeProfiles[0]);
        logger.info("Swagger地址：http://localhost:{}/doc.html", environment.getProperty("server.port"));
    }
}
