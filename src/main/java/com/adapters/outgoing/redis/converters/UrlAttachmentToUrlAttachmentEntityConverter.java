package com.adapters.outgoing.redis.converters;

import com.domain.process.engine.message.attachment.UrlAttachment;

public class UrlAttachmentToUrlAttachmentEntityConverter {
    public AttachmentEntity produceAttachmentEntity(UrlAttachment attachment) {
        if (attachment == null) {
            return new AttachmentEntity();
        }
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.type = attachment.type.toString();
        attachmentEntity.url = attachment.url.toString();

        return attachmentEntity;
    }
}
