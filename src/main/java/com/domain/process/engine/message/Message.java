package com.domain.process.engine.message;

import com.domain.process.engine.message.attachment.Attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr on 24.09.2017.
 */
public abstract class Message {
    protected String text;
    protected final String payload;
    protected List<Attachment> attachments = new ArrayList<>();

    public Message(String text, String payload) {
        this.text = text;
        this.payload = payload;
    }

    public Message(String text) {
        this.text = text;
        payload = null;
    }

    public Message() {
        payload = null;
    }

    public String getText() {
        return text;
    }

    public String getPayload() {
        return payload;
    }

    public boolean hasAttachments() {
        return !attachments.isEmpty();
    }

    public boolean hasAttachmentType(Attachment.Type type) {
        for (Attachment a : attachments) {
            if (a.type == type) return true;
        }
        return false;
    }

    public boolean hasPayload() {
        return  payload != null;
    }

    public boolean addAttachment(Attachment attachment) {
        return attachments.add(attachment);
    }

    public void removeAllAttachments() {
        attachments.clear();
    }

    public Attachment getAttachment(int index) {
        if (attachments.size() > 0) {
            return attachments.get(index);
        }
        return null;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
