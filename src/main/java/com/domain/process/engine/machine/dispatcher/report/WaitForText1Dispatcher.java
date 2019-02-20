package com.domain.process.engine.machine.dispatcher.report;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;

public class WaitForText1Dispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForText1Dispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        String text = message.getText().toUpperCase();
        if (text.length() != 0) {
            return MachineState.ASK_FOR_ADDITIONAL_IMAGES;
        }
        return MachineState.INVALID_INPUT_2;
    }
}
