package com.adapters.incoming.console.converters;


import com.domain.process.engine.message.OutgoingMessage;

public class OutgoingMessageToStringConverter {

    public String produceString(OutgoingMessage message) {
        return message.toString();
    }
}
