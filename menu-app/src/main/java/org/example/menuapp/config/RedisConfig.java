package org.example.menuapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {


//    @Primary
//    @Bean("springRedisConnectionFactory")
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName("localhost");
//        configuration.setPort(6379);
//        configuration.setDatabase(0);
//        return new LettuceConnectionFactory(configuration);
//    }
//
//
//    @Bean
//    public ObjectMapper redisObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        return objectMapper;
//    }
//
//    @Bean
//    public RedisSerializer<Object> redisValueSerializer() {
//        return new Jackson2JsonRedisSerializer<>(Object.class);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(redisValueSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(redisValueSerializer());
//        return template;
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(@Qualifier("springRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer()))
//                .entryTtl(Duration.ofMinutes(10));
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(configuration)
//                .build();
//    }
}
