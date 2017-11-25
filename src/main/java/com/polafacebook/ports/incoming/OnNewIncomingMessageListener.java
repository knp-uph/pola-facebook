package com.polafacebook.ports.incoming;

import com.polafacebook.process.engine.message.IncomingMessage;

public interface OnNewIncomingMessageListener {
    void onNewMessage(IncomingMessage incomingMessage);
}
