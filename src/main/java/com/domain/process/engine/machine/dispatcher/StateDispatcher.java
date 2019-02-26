package com.domain.process.engine.machine.dispatcher;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;

/**
 * Classes implementing this interface must be able to return the next destination based on incoming message.
 */
public interface StateDispatcher {
    public MachineState dispatch(Context context, IncomingMessage message);
}
