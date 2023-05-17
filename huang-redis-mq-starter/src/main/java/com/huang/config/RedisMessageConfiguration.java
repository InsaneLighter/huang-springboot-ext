package com.huang.config;

import com.huang.core.RedisMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Time 2023-05-17
 * Created by Huang
 * className: RedisMessageConfiguration
 * Description:
 */
@Configuration
public class RedisMessageConfiguration {
    @Value("${spring.redis.channel:default-channel}")
    private String channel;

    private RedisConnectionFactory redisConnectionFactory;
    private MessageListener messageListener;

    @Autowired
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Autowired
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        // 设置监听的频道或模式
        ChannelTopic channelTopic = new ChannelTopic(channel);
        container.addMessageListener(messageListener, channelTopic);
        return container;
    }

    @Bean
    public RedisMessageProducer redisMessageProducer(RedisTemplate<String, Object> redisTemplate) {
        return new RedisMessageProducer(redisTemplate);
    }
}
