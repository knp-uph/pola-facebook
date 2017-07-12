package com.polafacebook;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PolaFacebookApplication {

    private static final Logger logger = LoggerFactory.getLogger(PolaFacebookApplication.class);

    @Bean
    public MessengerSendClient messengerSendClient(@Value("${messenger4j.pageAccessToken}") String pageAccessToken) {
        logger.debug("Inicjalizacja MessengerSendClient - pageAccessToken: {}", pageAccessToken);
        return MessengerPlatform.newSendClientBuilder(pageAccessToken).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PolaFacebookApplication.class, args);
    }


}
