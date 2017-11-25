package com.adapters.dto.converters;

import com.adapters.dto.AttachmentEntity;
import com.adapters.dto.ContextEntity;
import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;

public class RedisContextToContextConverter {

    private UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter;

    public RedisContextToContextConverter(UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter) {
        this.attachmentConverter = attachmentConverter;
    }

    public Context produceContext(ContextEntity redisContext) {
        Context c = new Context(redisContext.userId, MachineState.valueOf(redisContext.state));

        for (AttachmentEntity a : redisContext.attachments) {
            c.attachments.add(attachmentConverter.produceUrlAttachment(a));
        }
        c.description = redisContext.description;
        c.result = redisContext.result;
        c.eanCode = redisContext.eanCode;
        if (c.lastAttachment != null) {
            c.lastAttachment = attachmentConverter.produceUrlAttachment(redisContext.lastAttachment);
        }
        c.lastText = redisContext.lastText;
        return c;
    }
}
