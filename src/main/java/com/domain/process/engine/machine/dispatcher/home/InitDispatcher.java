package com.domain.process.engine.machine.dispatcher.home;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;

public class InitDispatcher implements StateDispatcher {
    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        return MachineState.WELCOME;
    }
}
