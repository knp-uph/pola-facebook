package com.adapters.incoming.facebook;

import com.github.messenger4j.Messenger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Messenger4jConfiguration {

    @Bean
    public Messenger messenger() {
        return Messenger.create(System.getenv("messenger4j.pageAccessToken"), System.getenv("messenger4j.appSecret"), System.getenv("messenger4j.verifyToken"));
    }
}
