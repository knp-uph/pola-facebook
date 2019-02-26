package com.adapters.outgoing.redis.converters;

import com.domain.ports.dto.Attachment;
import com.domain.ports.dto.UrlAttachment;

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
