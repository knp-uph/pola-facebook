package com.adapters.dto.converters;

import com.adapters.dto.AttachmentEntity;
import com.polafacebook.process.engine.message.attachment.Attachment;
import com.polafacebook.process.engine.message.attachment.UrlAttachment;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlAttachmentEntityToUrlAttachmentConverter {
    public UrlAttachment produceUrlAttachment(AttachmentEntity attachmentEntity) {
        UrlAttachment urlAttachment = new UrlAttachment();
        urlAttachment.type = Attachment.Type.valueOf(attachmentEntity.type);
        try {
            urlAttachment.url = new URL(attachmentEntity.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlAttachment;
    }
}
