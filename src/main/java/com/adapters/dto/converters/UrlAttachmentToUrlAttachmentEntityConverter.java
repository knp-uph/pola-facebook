package com.adapters.dto.converters;

import com.adapters.dto.AttachmentEntity;
import com.polafacebook.process.engine.message.attachment.UrlAttachment;

public class UrlAttachmentToUrlAttachmentEntityConverter {
    public AttachmentEntity produceAttachmentEntity(UrlAttachment attachment) {
        if (attachment == null) {
            return new AttachmentEntity();
        }
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.type = attachment.toString();
        attachmentEntity.url = attachment.url.toString();

        return attachmentEntity;
    }
}
