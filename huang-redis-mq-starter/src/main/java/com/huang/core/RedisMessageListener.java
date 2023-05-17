package com.huang.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @Time 2023-05-17
 * Created by Huang
 * className: RedisMessageListener
 * Description: Redis消息监听器(示例)
 */
//@Component
public class RedisMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("接收到消息频道：{}", new String(message.getChannel()));
        logger.info("接收到消息内容：{}", new String(message.getBody()));
        logger.info("接收到消息模式：{}", new String(pattern));
    }
}
