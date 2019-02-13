package com.adapters.outgoing.redis.converters;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;

public class RedisContextToContextConverter {

    private UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter;

    public RedisContextToContextConverter(UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter) {
        this.attachmentConverter = attachmentConverter;
    }

    public Context produceContext(ContextEntity redisContext) {
        Context c = new Context(redisContext.userId, MachineState.valueOf(redisContext.state));

        for (AttachmentEntity a : redisContext.attachments) {
            c.getAttachments().add(attachmentConverter.produceUrlAttachment(a));
        }
        c.setDescription(redisContext.description);
        c.setProductInformation(redisContext.productInformation);
        c.setEanCode(redisContext.eanCode);
        if (c.getLastAttachment() != null) {
            c.setLastAttachment(attachmentConverter.produceUrlAttachment(redisContext.lastAttachment));
        }
        c.setLastText(redisContext.lastText);
        return c;
    }
}
