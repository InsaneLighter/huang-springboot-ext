# huang-nacos-config-refresh-starter
## 介绍
nacos配置中心自动刷新starter
## 使用说明
1. 引入依赖
```xml
<dependency>
    <groupId>com.huang</groupId>
    <artifactId>huang-nacos-config-refresh-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. 在配置文件中添加即可启用自动刷新功能
```yaml
spring:
  cloud:
    nacos:
      config:
        refresh-enabled: true
```