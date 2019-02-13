package com.domain.ports.outgoing.communicator;

import com.domain.process.engine.message.OutgoingMessage;

public interface OnNewOutgoingMessageListener {
    void onNewMessage(OutgoingMessage message);
}
