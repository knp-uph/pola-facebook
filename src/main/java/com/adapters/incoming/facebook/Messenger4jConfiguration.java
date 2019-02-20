package com.adapters.incoming.facebook;

import com.domain.ApplicationConfig;
import com.domain.ports.incoming.communicator.OnNewIncomingMessageListener;
import com.github.messenger4j.Messenger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ApplicationConfig.class})
@Configuration
public class Messenger4jConfiguration {

    @Bean
    public Messenger messenger() {
        return Messenger.create(System.getenv("messenger4j.pageAccessToken"), System.getenv("messenger4j.appSecret"), System.getenv("messenger4j.verifyToken"));
    }

    @Bean
    public FacebookEventHandler facebookEventHandler(OnNewIncomingMessageListener onNewIncomingMessageListener, Messenger messenger) {
        return new FacebookEventHandler(onNewIncomingMessageListener, messenger);
    }

}
