package com.adapters.outgoing.redis.converters;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.message.attachment.Attachment;
import com.domain.process.engine.message.attachment.UrlAttachment;

public class ContextToRedisContextConverter {

    private UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter;

    public ContextToRedisContextConverter(UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter) {
        this.attachmentConverter = attachmentConverter;
    }

    public ContextEntity produceRedisContext(Context context) {
        ContextEntity c = new ContextEntity();
        c.userId = context.getUserId();
        for (Attachment a : context.getAttachments()) {
            if (a instanceof UrlAttachment) {
                c.attachments.add(attachmentConverter.produceAttachmentEntity((UrlAttachment) a));
            }
        }
        c.description = context.getDescription();
        c.productInformation = context.getProductInformation();
        c.state = context.getState().toString();
        c.eanCode = context.getEanCode();
        c.lastAttachment = attachmentConverter.produceAttachmentEntity((UrlAttachment) context.getLastAttachment());
        c.lastText = context.getLastText();
        return c;
    }
}
