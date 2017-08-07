package com.polafacebook.engine.query;

import java.util.Date;

/**
 * Created by Piotr on 07.08.2017.
 */
public class PlatformQuery {
    protected Date timestamp;
    protected String senderId;
    protected String payload;

    public PlatformQuery() {
        timestamp = null;
        senderId = null;
        payload = null;
    }

    public PlatformQuery(Date timestamp, String senderId, String payload) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.payload = payload;
    }

    public PlatformQuery(Date timestamp, String senderId) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.payload = null;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getPayload() {
        return payload;
    }
}
