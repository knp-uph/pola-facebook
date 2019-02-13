package com.domain.process.engine;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.message.IncomingMessage;

/**
 * Created by Piotr on 07.08.2017.
 */
public abstract class AbstractEngine {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext(Context context) {
        return context;
    }

    public abstract void doAction(IncomingMessage incomingMessage);
}
