package com.domain.process.engine.machine.dispatcher.suggestion;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;

public class WaitForText2Dispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForText2Dispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        String text = message.getText().toUpperCase();
        if (text.length() != 0) {
            return MachineState.SAVE_SUGGESTION;
        }
        return MachineState.INVALID_INPUT_4;
    }
}
