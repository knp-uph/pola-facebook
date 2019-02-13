
package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.process.engine.ConversationEngine;
import com.domain.process.engine.message.OutgoingMessage;
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

    private final OnNewOutgoingMessageListener listener;

    private ConversationEngine conversationEngine;

    public ControllerExceptionHandler(OnNewOutgoingMessageListener listener, ConversationEngine conversationEngine) {
        this.listener = listener;
        this.conversationEngine = conversationEngine;
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

        String id = conversationEngine.getCurrentId();

        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ControllerExceptionHandler.text, id);
        outgoingMessage.addQuickReply(BotResponses.ControllerExceptionHandler.quickReply, "INIT");

        listener.onNewMessage(outgoingMessage);
        logger.debug("Error message dispatched.");
    }
}
