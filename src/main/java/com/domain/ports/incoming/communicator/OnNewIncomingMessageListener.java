package com.domain.ports.incoming.communicator;

public interface OnNewIncomingMessageListener {
    void onNewMessage(IncomingMessage incomingMessage);

    FeatureConfiguration getFeatureConfiguration();
}
