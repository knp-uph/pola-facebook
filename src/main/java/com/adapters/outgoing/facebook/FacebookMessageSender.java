package com.adapters.outgoing.facebook;

import com.domain.ports.outgoing.communicator.Action;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.communicator.OutgoingMessage;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.SenderActionPayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.senderaction.SenderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class FacebookMessageSender implements OnNewOutgoingMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(FacebookMessageSender.class);

    private final Messenger messenger;
    private final TextSplitter splitter;

    public FacebookMessageSender(Messenger messenger, TextSplitter splitter) {
        this.messenger = messenger;
        this.splitter = splitter;
    }

    private void sendTextMessage(String recipientId, String text, List<QuickReply> quickReplies) {
        logger.debug("SendTextMessage: " + recipientId + ":" + text);

        try {
            final IdRecipient recipient = IdRecipient.create(recipientId);
            final NotificationType notificationType = NotificationType.REGULAR;

            Iterator<String> iterator = splitter.split(text).iterator();

            while (iterator.hasNext()) {
                this.sendAction(recipientId, Action.TYPING_ON);
                String textPart = iterator.next();

                final TextMessage textMessage = TextMessage.create(textPart);
                final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage);

                if (iterator.hasNext()) {
                    this.messenger.send(messagePayload);
                } else if (quickReplies != null) {
                    final TextMessage textMessageWithQuickReplies = TextMessage.create(textPart, of(quickReplies), empty());
                    final MessagePayload messagePayloadWithQuickReplies = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessageWithQuickReplies);
                    this.messenger.send(messagePayloadWithQuickReplies);
                } else {
                    this.messenger.send(messagePayload);
                }

            }
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    private void sendWithQuickReplies(OutgoingMessage fromEngineMessage) {
        List<QuickReply> quickReplies = new ArrayList<>();

        for (com.domain.ports.outgoing.communicator.QuickReply quickReply : fromEngineMessage.getQuickReplies()) {
            quickReplies.add(TextQuickReply.create(quickReply.title, quickReply.value));
        }

        final String text = fromEngineMessage.getText();
        final String recipientId = fromEngineMessage.getRecipientId();

        this.sendTextMessage(recipientId, text, quickReplies);
    }

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error has occurred.", e);
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
            SenderAction senderAction;
            if (action == Action.TYPING_ON) {
                senderAction = SenderAction.TYPING_ON;
            } else if (action == Action.TYPING_OFF) {
                senderAction = SenderAction.TYPING_OFF;
            } else if (action == Action.MARK_SEEN) {
                senderAction = SenderAction.MARK_SEEN;
            } else {
                throw new IllegalArgumentException("Unsupported domain send-action type: " + action.toString());
            }
            this.messenger.send(SenderActionPayload.create(recipientId, senderAction));
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }
}
