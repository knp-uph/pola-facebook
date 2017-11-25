package com.polafacebook.ports.outgoing;

import com.polafacebook.process.engine.message.OutgoingMessage;

public interface OnNewOutgoingMessageListener {
    void onNewMessage(OutgoingMessage message);
}
