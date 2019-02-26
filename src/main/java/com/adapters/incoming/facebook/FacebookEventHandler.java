package com.adapters.incoming.facebook;

import com.domain.ports.dto.UrlAttachment;
import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.incoming.communicator.OnNewIncomingMessageListener;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.SenderActionPayload;
import com.github.messenger4j.send.senderaction.SenderAction;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.AttachmentMessageEvent;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.github.messenger4j.webhook.event.attachment.Attachment;
import com.github.messenger4j.webhook.event.attachment.RichMediaAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Instant;
import java.util.List;

import static com.domain.ports.dto.Attachment.Type.IMAGE;

/**
 * This handler is responsible for the conversion of messenger4j's events to messages digestible by the engine.
 */
public class FacebookEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(FacebookEventHandler.class);

    private final OnNewIncomingMessageListener conversationEngine;
    private final Messenger messenger;

    private Instant lastTimestampServed = Instant.now();

    public FacebookEventHandler(OnNewIncomingMessageListener conversationEngine, Messenger messenger) {
        this.conversationEngine = conversationEngine;
        this.messenger = messenger;
    }

    private boolean hasBeenServed(Instant timestamp) {
        boolean result = timestamp.compareTo(lastTimestampServed) <= 0;
        lastTimestampServed = timestamp;
        return result;
    }

    private void promptEngine(IncomingMessage message) {
        conversationEngine.onNewMessage(message);
    }

    private void sendMarkSeen(String senderId) {
        try {
            this.messenger.send(SenderActionPayload.create(senderId, SenderAction.MARK_SEEN));
        } catch (MessengerApiException | MessengerIOException e) {
            logger.error("MARK_SEEN could not be sent. An unexpected error occurred.", e);
        }
    }

    public void onTextMessageEvent(TextMessageEvent event) {
        final Instant timestamp = event.timestamp();
        if (this.hasBeenServed(timestamp)) {
            logger.debug("Discarding duplicate event: " + event.messageId() + " with timestamp " + event.timestamp(), event);
            return;
        }

        final String messageText = event.text();
        final String senderId = event.senderId();

        IncomingMessage toEngineMessage = new IncomingMessage(messageText, senderId);
        sendMarkSeen(senderId);

        this.promptEngine(toEngineMessage);
    }

    public void onQuickReplyMessageEvent(QuickReplyMessageEvent event) {
        final Instant timestamp = event.timestamp();
        if (this.hasBeenServed(timestamp)) {
            logger.debug("Discarding a duplicate event: ", event);
            return;
        }

        final String senderId = event.senderId();
        final String text = event.text();
        final String quickReplyPayload = event.payload();

        IncomingMessage toEngineMessage = new IncomingMessage(text, senderId, quickReplyPayload);
        sendMarkSeen(senderId);

        this.promptEngine(toEngineMessage);
    }

    public void onAttachmentMessageEvent(AttachmentMessageEvent event) {
        final Instant timestamp = event.timestamp();
        if (this.hasBeenServed(timestamp)) {
            logger.debug("Discarding a duplicate event: ", event);
            return;
        }

        final String senderId = event.senderId();

        final List<Attachment> attachments = event.attachments();

        attachments.forEach(attachment -> {
            IncomingMessage toEngineMessage = new IncomingMessage("", senderId);

            if (attachment.isRichMediaAttachment()) {
                final RichMediaAttachment richMediaAttachment = attachment.asRichMediaAttachment();
                final RichMediaAttachment.Type type = richMediaAttachment.type();
                final URL url = richMediaAttachment.url();
                logger.debug("Received rich media attachment of type '{}' with url: {}", type, url);

                switch (type) {
                    case IMAGE:
                        toEngineMessage.addAttachment(new UrlAttachment(IMAGE, url));
                        break;
                    default:
                        logger.debug("Unsupported attachment type received: " + type);
                }
            }

            this.promptEngine(toEngineMessage);
        });
    }

    public void onPostbackEvent(PostbackEvent event) {
        final Instant timestamp = event.timestamp();
        if (this.hasBeenServed(timestamp)) {
            logger.debug("Discarding a duplicate event: ", event);
            return;
        }

        logger.debug("Received PostbackEvent: {}", event);

        final String senderId = event.senderId();
        final String recipientId = event.recipientId();
        final String payload = (event.payload().isPresent() ? event.payload().get() : "null");

        logger.info("Received postback for user '{}' and page '{}' with payload '{}' at '{}'",
                senderId, recipientId, payload, timestamp);

        this.promptEngine(new IncomingMessage("", senderId, payload));
    }

    public void onFallback(Event event) {
        final String senderId = event.senderId();
        logger.info("Received unsupported message from user '{}'", senderId);
    }


}
