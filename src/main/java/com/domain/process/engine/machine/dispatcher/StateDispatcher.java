package com.domain.process.engine.machine.dispatcher;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.message.IncomingMessage;

/**
 * Classes implementing this interface must be able to return the next destination based on the incoming message.
 */
public interface StateDispatcher {
    public MachineState dispatch(Context context, IncomingMessage message);
}
