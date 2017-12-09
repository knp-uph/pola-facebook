package com.adapters.outgoing.facebook;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.*;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.message.Action;
import com.polafacebook.process.engine.message.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class FacebookMessageSender implements OnNewOutgoingMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(FacebookMessageSender.class);
    private final MessengerSendClient sendClient;
    private final TextSplitter splitter;

    public FacebookMessageSender(MessengerSendClient sendClient) {
        this.sendClient = sendClient;
        this.splitter = new TextSplitter(320);
    }

    private void sendTextMessage(String recipientId, String text, List<QuickReply> quickReplies) {
        logger.debug("SendTextMessage: " + recipientId + ":" + text);

        try {
            final Recipient recipient = Recipient.newBuilder().recipientId(recipientId).build();
            final NotificationType notificationType = NotificationType.REGULAR;
            final String metadata = "DEVELOPER_DEFINED_METADATA";

            Iterator<String> iterator = splitter.split(text).iterator();

            while (iterator.hasNext()) {
                this.sendClient.sendSenderAction(recipient, NotificationType.NO_PUSH, SenderAction.TYPING_ON);
                String msg = iterator.next();

                if (iterator.hasNext()) {
                    this.sendClient.sendTextMessage(recipient, notificationType, msg, metadata);
                } else if (quickReplies != null) {
                    this.sendClient.sendTextMessage(recipient, notificationType, msg, quickReplies, metadata);
                } else {
                    this.sendClient.sendTextMessage(recipient, notificationType, msg, metadata);
                }
            }
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    private void sendWithQuickReplies(OutgoingMessage fromEngineMessage) {
        List<QuickReply> quickReplies;
        QuickReply.ListBuilder listBuilder = com.github.messenger4j.send.QuickReply.newListBuilder();

        for (com.polafacebook.process.engine.message.QuickReply quickReply : fromEngineMessage.getQuickReplies()) {
            listBuilder.addTextQuickReply(quickReply.title, quickReply.value).toList();
        }

        quickReplies = listBuilder.build();

        String text = fromEngineMessage.getText();
        String recipientId = fromEngineMessage.getRecipientId();

        this.sendTextMessage(recipientId, text, quickReplies);
    }

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }

    @Override
    public void onNewMessage(OutgoingMessage message) {
        logger.debug("Sending new message: " + message);

        if (message.hasQuickReplies()) {
            sendWithQuickReplies(message);
        } else {
            if (message.hasAction()) {
                this.sendAction(message.getRecipientId(), message.getAction());
            } else {
                this.sendTextMessage(message.getRecipientId(), message.getText());
            }
        }
    }

    private void sendTextMessage(String recipientId, String text) {
        sendTextMessage(recipientId, text, null);
    }

    private void sendAction(String recipientId, Action action) {
        try {
            this.sendClient.sendSenderAction(recipientId, SenderAction.valueOf(action.toString()));
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }
}
