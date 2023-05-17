/*
package com.huang;

import com.alibaba.fastjson.JSONObject;
import com.huang.core.RedisMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

*/
/**
 * @Time 2023-05-17
 * Created by Huang
 * className: MainTest
 * Description:
 *//*

@SpringBootTest(classes = Main.class)
public class MainTest {
    private static final String CHANNEL = "default-channel";

    private RedisMessageProducer redisMessageProducer;

    @Autowired
    public void setRedisMessageProducer(RedisMessageProducer redisMessageProducer) {
        this.redisMessageProducer = redisMessageProducer;
    }

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
}
*/
