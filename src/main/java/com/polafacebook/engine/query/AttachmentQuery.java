package com.polafacebook.engine.query;

import java.util.Date;

/**
 * Created by Piotr on 07.08.2017.
 */
public class AttachmentQuery extends PlatformQuery {
    public static enum AttachmentType {
        IMAGE, AUDIO, VIDEO, FILE, LOCATION;
    }

    public AttachmentType type;

    public AttachmentQuery(AttachmentType type) {
        this.type = type;
    }

    public AttachmentQuery(Date timestamp, String senderId, String payload, AttachmentType type) {
        super(timestamp, senderId, payload);
        this.type = type;
    }

    public AttachmentQuery(Date timestamp, String senderId, AttachmentType type) {
        super(timestamp, senderId);
        this.type = type;
    }
}
