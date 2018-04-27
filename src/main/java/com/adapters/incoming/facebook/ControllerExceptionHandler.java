
package com.adapters.incoming.facebook;


import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.polafacebook.process.engine.ConversationEngine;
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
    private final MessengerSendClient sendClient;

    private ConversationEngine conversationEngine;

    public ControllerExceptionHandler(MessengerSendClient sendClient, ConversationEngine conversationEngine) {
        this.sendClient = sendClient;
        this.conversationEngine = conversationEngine;
        Sentry.init();
    }

    @ResponseStatus(HttpStatus.OK)  // 200, we're pretending everything's fine <insert meme here>
    @ExceptionHandler(Exception.class)
    public void handleDefaultError(Exception e) {
        logger.error("The application has thrown something!", e);
        try {
            try {
                conversationEngine.resetState();
            } catch (Exception internalError) {
                logger.error("Failed to reset conversation state.", internalError);
            }

            String id = conversationEngine.getCurrentId();

            //TODO: put this elsewhere
            QuickReply.ListBuilder listBuilder = com.github.messenger4j.send.QuickReply.newListBuilder();
            listBuilder.addTextQuickReply("Spróbujmy od nowa.", "INIT").toList();
            this.sendClient.sendTextMessage(
                    id,
                    "Przepraszamy; wystąpił po naszej stronie nieoczekiwany problem. Nasze stado małp-programistów pracuje nad rozwiązaniem! Spróbuj ponownie później.",
                    listBuilder.build());
        } catch (MessengerApiException | MessengerIOException e1) {
            e1.printStackTrace();
        }
    }
}
