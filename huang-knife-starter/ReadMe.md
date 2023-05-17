# huang-knife-starter
## 介绍
knife-swagger页面自动配置
## 使用说明
1. 引入依赖
```xml
<dependency>
    <groupId>com.huang</groupId>
    <artifactId>huang-knife-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. 在配置文件中添加即可启用swagger页面
```yaml
knife:
  swagger:
    enabled: true
    title: huang-knife-starter
    description: huang-knife-starter
    version: 1.0.0
    base-package: com.huang
    contact:
      name: huang
      url: huanghong.top
      email: mail@huanghong.top
    license: Apache License 2.0
    license-url: http://www.apache.org/licenses/LICENSE-2.0.html
    terms-of-service-url: https://www.huanghong.top
```