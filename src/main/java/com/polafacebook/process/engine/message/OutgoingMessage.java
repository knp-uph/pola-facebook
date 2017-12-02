package com.polafacebook.process.engine.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr on 07.08.2017.
 */
public class OutgoingMessage extends Message {
    private final String recipientId;
    private List<QuickReply> quickReplies = new ArrayList<>();

    public OutgoingMessage(String text, String recipientId) {
        super(text);
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void addQuickReply(String title, String value) {
        this.quickReplies.add(new QuickReply(title, value));
    }

    public boolean hasQuickReplies() {
        return !quickReplies.isEmpty();
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "recipientId='" + recipientId + '\'' +
                ", quickReplies=" + quickReplies +
                ", text='" + text + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
