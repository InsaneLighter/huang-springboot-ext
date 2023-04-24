package com.huang.config;

import com.huang.properties.KnifeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Time 2023-04-23
 * Created by Huang
 * className: com.huang.config.Knife4jConfiguration
 * Description:
 */
@Configuration
@EnableOpenApi
@EnableConfigurationProperties(KnifeProperties.class)
public class Knife4jConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = true)
    public Docket docket(ApiInfo apiInfo) {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo).enable(true)
                .select()
                //apis： 添加swagger接口提取范围
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        System.out.println("" +
                "    //    / /                                           //   / /                   //  ) )     \n" +
                "   //___ / /           ___       __      ___           //__ / /      __     ( ) __//__  ___    \n" +
                "  / ___   / //   / / //   ) ) //   ) ) //   ) ) ____  //__  /     //   ) ) / /   //   //___) ) \n" +
                " //    / / //   / / //   / / //   / / ((___/ /       //   \\ \\  //   / / / /   //   //        \n" +
                "//    / / ((___( ( ((___( ( //   / /   //__         //     \\ \\//   / / / /   //   ((____     \n");
        return docket;
    }

    @Bean
    public ApiInfo apiInfo(KnifeProperties knifeProperties) {
        String title = knifeProperties.getTitle();
        String description = knifeProperties.getDescription();
        String version = knifeProperties.getVersion();
        String termsOfServiceUrl = knifeProperties.getTermsOfServiceUrl();
        String license = knifeProperties.getLicense();
        String licenseUrl = knifeProperties.getLicenseUrl();
        Contact contact = knifeProperties.getContact();
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .termsOfServiceUrl(termsOfServiceUrl)
                .license(license)
                .licenseUrl(licenseUrl)
                .contact(contact)
                .build();
    }
}
