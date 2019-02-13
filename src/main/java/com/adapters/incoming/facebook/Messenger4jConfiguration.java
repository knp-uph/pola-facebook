package com.adapters.incoming.facebook;

import com.domain.BotResponses;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.greeting.Greeting;
import com.github.messenger4j.messengerprofile.greeting.LocalizedGreeting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Configuration
public class Messenger4jConfiguration {

    @Bean
    public Messenger messenger() {
        return Messenger.create(System.getenv("messenger4j.pageAccessToken"), System.getenv("messenger4j.appSecret"), System.getenv("messenger4j.verifyToken"));
    }

    @Bean
    public Greeting greeting() {
        return Greeting.create(BotResponses.Setup.greetingText, LocalizedGreeting.create(SupportedLocale.pl_PL,
                BotResponses.Setup.greetingText));
    }

    @Bean
    public StartButton startButton() {
        return StartButton.create("INIT");
    }

    @Bean
    public MessengerSettings messengerSettings(Greeting greeting) {
        return MessengerSettings.create(of(startButton()), of(greeting), empty(),
                empty(), empty(), empty(), empty());
    }
}
