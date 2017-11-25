package com.polafacebook.process.engine.message;

/**
 * Created by Piotr on 23.09.2017.
 */
public class IncomingMessage extends Message {
    private String senderId;
    private String payload;

    public IncomingMessage() {
    }

    public IncomingMessage(String text, String senderId) {
        super(text);
        this.senderId = senderId;
    }

    public IncomingMessage(String text, String senderId, String payload) {
        super(text);
        this.senderId = senderId;
        this.payload = payload;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "senderId='" + senderId + '\'' +
                ", text='" + text + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
