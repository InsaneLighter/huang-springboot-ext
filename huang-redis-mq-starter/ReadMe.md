# huang-redis-mq-starter
springboot整合redis实现消息队列
## 1.引入依赖
```xml
<dependency>
    <groupId>com.huang</groupId>
    <artifactId>huang-redis-mq-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
## 2.配置文件
```yaml
spring:
  redis:
    # 消息通道 默认值: default-channel
    channel: default-channel 
```
## 3.使用
```java
// 注入消息发布者
@Autowired
private RedisMessageProducer redisMessageProducer;

// 发布消息
@Test
public void sendMessage() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "Huang");
    jsonObject.put("age", 18);
    Map<String, Object> map = jsonObject.getInnerMap();
    redisMessageProducer.sendMessage(CHANNEL, map.toString());
    //接收到消息频道：default-channel
    //接收到消息内容："{name=Huang, age=18}"
    //接收到消息模式：default-channel
}
```
## 4.消息接收
```java
//自定义消息接收者
@Component
public class MyRedisMessageListener implements MessageListener {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("接收到消息频道：" + channel);
        System.out.println("接收到消息内容：" + message);
        System.out.println("接收到消息模式：" + channel);
    }
}
```
