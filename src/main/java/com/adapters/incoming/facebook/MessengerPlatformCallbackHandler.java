package com.adapters.incoming.facebook;

import com.domain.ports.incoming.communicator.CommunicatorConfigurationProvider;
import com.domain.ports.incoming.communicator.FeatureConfiguration;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.greeting.Greeting;
import com.github.messenger4j.messengerprofile.greeting.LocalizedGreeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Created by Jakub on 12.07.2017.
 */


@RestController
@RequestMapping("/callback")
public class MessengerPlatformCallbackHandler {

    //"https://raw.githubusercontent.com/fbsamples/messenger-platform-samples/master/node/public";

    private static final Logger logger = LoggerFactory.getLogger(MessengerPlatformCallbackHandler.class);

    private final Messenger messenger;

    private final FacebookEventHandler eventHandler;

    private final CommunicatorConfigurationProvider communicatorConfigurationProvider;

    @Autowired
    public MessengerPlatformCallbackHandler(final Messenger messenger,
                                            final FacebookEventHandler eventHandler,
                                            CommunicatorConfigurationProvider communicatorConfigurationProvider) {
        this.messenger = messenger;
        this.eventHandler = eventHandler;
        this.communicatorConfigurationProvider = communicatorConfigurationProvider;
        try {
            FeatureConfiguration featureConfiguration = this.communicatorConfigurationProvider.getFeatureConfiguration();

            Greeting greeting = Greeting.create(featureConfiguration.getGreetingText(), LocalizedGreeting.create(SupportedLocale.pl_PL, featureConfiguration.getGreetingText()));
            StartButton startButton = StartButton.create(featureConfiguration.getInitializationPayload());

            messenger.updateSettings(MessengerSettings.create(of(startButton), of(greeting), empty(),
                    empty(), empty(), empty(), empty()));
        } catch (MessengerApiException e) {
            logger.error("Unable to set Messenger settings.", e);
        } catch (MessengerIOException e) {
            logger.error("Error setting up callback.", e);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken,
                                                @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {

        logger.info("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode,
                verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.error("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload,
                                               @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {

        try {
            this.messenger.onReceiveEvents(payload, of(signature), event -> {
                logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
                if (event.isTextMessageEvent()) {
                    eventHandler.onTextMessageEvent(event.asTextMessageEvent());
                } else if (event.isAttachmentMessageEvent()) {
                    eventHandler.onAttachmentMessageEvent(event.asAttachmentMessageEvent());
                } else if (event.isQuickReplyMessageEvent()) {
                    eventHandler.onQuickReplyMessageEvent(event.asQuickReplyMessageEvent());
                } else if (event.isPostbackEvent()) {
                    eventHandler.onPostbackEvent(event.asPostbackEvent());
                } else {
                    eventHandler.onFallback(event);
                }
            });
            logger.debug("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}