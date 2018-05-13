package com.polafacebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PolaFacebookApplication {

    private static final Logger logger = LoggerFactory.getLogger(PolaFacebookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PolaFacebookApplication.class, args);
    }


}
