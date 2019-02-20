package com.domain.process.engine;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.incoming.communicator.OnNewIncomingMessageListener;
import com.domain.ports.outgoing.context.Context;

public abstract class AbstractEngine implements OnNewIncomingMessageListener {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Prompts the conversation engine to respond to stimuli.
     *
     * @param incomingMessage
     * @return
     */
    public abstract void onNewMessage(IncomingMessage incomingMessage);

    /**
     * Resets state of the current conversation, so it can be cleaned and started anew.
     */
    public abstract void resetState();

    public abstract String getCurrentUserId();
}
