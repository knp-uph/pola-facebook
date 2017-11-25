package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

/**
 * Classes implementing this interface must be able to return the next destination based on the incoming message.
 */
public interface StateDispatcher {
    public MachineState dispatch(Context context, IncomingMessage message);
}
