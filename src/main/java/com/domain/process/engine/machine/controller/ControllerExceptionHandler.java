
package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.incoming.communicator.CommunicatorConfigurationProvider;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.communicator.OutgoingMessage;
import com.domain.process.engine.AbstractEngine;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final OnNewOutgoingMessageListener onNewOutgoingMessageListener;

    private final AbstractEngine conversationEngine;

    private final CommunicatorConfigurationProvider communicatorConfigurationProvider;

    public ControllerExceptionHandler(OnNewOutgoingMessageListener onNewOutgoingMessageListener,
                                      AbstractEngine conversationEngine,
                                      CommunicatorConfigurationProvider communicatorConfigurationProvider) {
        this.onNewOutgoingMessageListener = onNewOutgoingMessageListener;
        this.conversationEngine = conversationEngine;
        this.communicatorConfigurationProvider = communicatorConfigurationProvider;
        Sentry.init();
    }

    @ResponseStatus(HttpStatus.OK)  // 200, we're pretending everything's fine <insert meme here>
    @ExceptionHandler(Exception.class)
    public void handleDefaultError(Exception e) {
        logger.error("The application has thrown something!", e);
        try {
            conversationEngine.resetState();
        } catch (Exception internalError) {
            logger.error("Failed to reset conversation state.", internalError);
        }

        String id = conversationEngine.getCurrentUserId();

        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ControllerExceptionHandler.text, id);
        outgoingMessage.addQuickReply(BotResponses.ControllerExceptionHandler.quickReply,
                communicatorConfigurationProvider.getFeatureConfiguration().getInitializationPayload());

        onNewOutgoingMessageListener.onNewMessage(outgoingMessage);
        logger.debug("Error message dispatched.");
    }
}
