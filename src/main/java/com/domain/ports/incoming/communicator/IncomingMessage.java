package com.domain.ports.incoming.communicator;

import com.domain.ports.dto.Message;

public class IncomingMessage extends Message {
    private String senderId;

    public IncomingMessage() {
    }

    public IncomingMessage(String text, String senderId) {
        super(text);
        this.senderId = senderId;
    }

    public IncomingMessage(String text, String senderId, String payload) {
        super(text, payload);
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "senderId='" + senderId + '\'' +
                ", payload='" + payload + '\'' +
                ", text='" + text + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
