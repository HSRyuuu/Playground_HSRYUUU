package com.hsryuuu.traffic.fcfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class FcfsRedisConfig {

    @Bean
    public DefaultRedisScript<Long> stockDecrScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText("""
                local stock = tonumber(redis.call('GET', KEYS[1]))
                if stock == nil then return -1 end
                if stock <= 0 then return 0 end
                return redis.call('DECR', KEYS[1])
                """);
        script.setResultType(Long.class);
        return script;
    }
}
