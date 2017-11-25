package com.adapters.dto.converters;

import com.adapters.dto.ContextEntity;
import com.polafacebook.model.Context;
import com.polafacebook.process.engine.message.attachment.Attachment;
import com.polafacebook.process.engine.message.attachment.UrlAttachment;

public class ContextToRedisContextConverter {

    private UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter;

    public ContextToRedisContextConverter(UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter) {
        this.attachmentConverter = attachmentConverter;
    }

    //TODO: desnowflake Url Attachment
    public ContextEntity produceRedisContext(Context context) {
        ContextEntity c = new ContextEntity();
        c.userId = context.userId;
        for (Attachment a : context.attachments) {
            if (a instanceof UrlAttachment) {
                c.attachments.add(attachmentConverter.produceAttachmentEntity((UrlAttachment) a));
            }
        }
        c.description = context.description;
        c.result = context.result;
        c.state = context.state.toString();
        c.eanCode = context.eanCode;
        c.lastAttachment = attachmentConverter.produceAttachmentEntity((UrlAttachment) context.lastAttachment);
        c.lastText = context.lastText;
        return c;
    }
}
