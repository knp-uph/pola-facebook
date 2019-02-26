package com.domain.ports.outgoing.communicator;

import com.domain.ports.dto.Message;

import java.util.ArrayList;
import java.util.List;

public class OutgoingMessage extends Message {
    private final String recipientId;
    private List<QuickReply> quickReplies = new ArrayList<>();
    private final Action action;

    public OutgoingMessage(String text, String recipientId) {
        super(text);
        this.recipientId = recipientId;
        action = null;
    }

    public OutgoingMessage(Action action, String recipientId) {
        this.recipientId = recipientId;
        this.action = action;
    }

    public OutgoingMessage(String text, String payload, String recipientId) {
        super(text, payload);
        this.recipientId = recipientId;
        action = null;
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

    public boolean hasAction() {
        return action != null;
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "recipientId='" + recipientId + '\'' +
                ", quickReplies=" + quickReplies +
                ", action=" + action +
                '}';
    }
}
