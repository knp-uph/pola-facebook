package com.adapters.incoming.console.converters;

import com.adapters.incoming.console.SpecialArgumentParser;
import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.process.engine.message.attachment.Attachment;
import com.domain.process.engine.message.attachment.FileAttachment;

import java.util.Map;

/**
 * Created by Piotr on 24.09.2017.
 */
public class StringToIncomingMessageConverter {
    private String senderId;

    public StringToIncomingMessageConverter(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public IncomingMessage produceIncomingMessage(String line) {
        SpecialArgumentParser parser = new SpecialArgumentParser(line);

        IncomingMessage message = new IncomingMessage(parser.getHeaderText(), senderId);

        for (Map.Entry<String, String> a : parser.getArguments().entrySet()) {
            switch(a.getKey().toLowerCase()){
                case "img":
                case "image":
                    message.addAttachment(new FileAttachment(a.getValue(), Attachment.Type.IMAGE));
                    break;
            }
        }
        return message;
    }
}
