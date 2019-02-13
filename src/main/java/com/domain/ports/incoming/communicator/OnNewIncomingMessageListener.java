package com.domain.ports.incoming.communicator;

import com.domain.process.engine.message.IncomingMessage;

public interface OnNewIncomingMessageListener {
    void onNewMessage(IncomingMessage incomingMessage);
}
