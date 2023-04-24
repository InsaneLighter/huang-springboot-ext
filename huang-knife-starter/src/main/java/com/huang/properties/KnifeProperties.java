package com.huang.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import springfox.documentation.service.Contact;

/**
 * @Time 2023-04-24
 * Created by Huang
 * className: KnifeProperties
 * Description:
 */
@ConfigurationProperties(prefix = "knife")
public class KnifeProperties {
    public final static String DEFAULT_TITLE = "Huang-web";
    public final static String DEFAULT_DESCRIPTION = "create by Huang";
    public final static String DEFAULT_VERSION = "v1.0";
    public final static String DEFAULT_TERMS_OF_SERVICE_URL = "http://localhost:8080";
    public final static String DEFAULT_LICENSE = "Apache License Version 2.0";
    public final static String DEFAULT_LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
    public final static String DEFAULT_CONTACT_NAME = "Huang";
    public final static String DEFAULT_CONTACT_URL = "https://huanghong.top";
    public final static String DEFAULT_CONTACT_EMAIL = "77376447@qq.com";

    //swagger 标题
    private String title;
    // swagger 描述
    private String description;
    // swagger 服务条款网址
    private String termsOfServiceUrl;
    // swagger 联系人
    private Contact contact;
    // swagger 许可证
    private String license;
    // swagger 许可证地址
    private String licenseUrl;
    // swagger 版本
    private String version;

    public String getTitle() {
        if(!StringUtils.hasText(title)) {
            return DEFAULT_TITLE;
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if(!StringUtils.hasText(description)) {
            return DEFAULT_DESCRIPTION;
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        if(!StringUtils.hasText(termsOfServiceUrl)) {
            return DEFAULT_TERMS_OF_SERVICE_URL;
        }
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public Contact getContact() {
        if(contact == null) {
            contact = new Contact(DEFAULT_CONTACT_NAME, DEFAULT_CONTACT_URL, DEFAULT_CONTACT_EMAIL);
        }
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getLicense() {
        if(!StringUtils.hasText(license)) {
            return DEFAULT_LICENSE;
        }
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        if(!StringUtils.hasText(licenseUrl)) {
            return DEFAULT_LICENSE_URL;
        }
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getVersion() {
        if(!StringUtils.hasText(version)) {
            return DEFAULT_VERSION;
        }
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
