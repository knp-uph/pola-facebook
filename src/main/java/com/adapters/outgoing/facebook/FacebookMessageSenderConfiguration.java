package com.adapters.outgoing.facebook;

import com.github.messenger4j.Messenger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookMessageSenderConfiguration {

    @Bean
    TextSplitter textSplitter() {
        return new TextSplitter(320);
    }

    @Bean
    FacebookMessageSender facebookMessageSender(Messenger messenger, TextSplitter textSplitter) {
        return new FacebookMessageSender(messenger, textSplitter);
    }

}
