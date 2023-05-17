package com.huang.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Time 2023-05-17
 * Created by Huang
 * className: RedisMessageProducer
 * Description:
 */
public class RedisMessageProducer {
    private final Logger logger = LoggerFactory.getLogger(RedisMessageProducer.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageProducer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送消息
     *
     * @param channel 频道
     * @param message 消息
     */
    public void sendMessage(String channel, Object message) {
        logger.info("发送消息到频道：{}，消息：{}", channel, message);
        redisTemplate.convertAndSend(channel, message);
        logger.info("消息发送成功");
    }
}
