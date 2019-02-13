package com.domain.process.engine.machine.dispatcher.report;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.message.IncomingMessage;

public class WaitForDecisionDispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForDecisionDispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        if (dispatcherHelper.isAffirmative(message)) {
            return MachineState.ASK_FOR_IMAGE;
        } else if (dispatcherHelper.isNegative(message)) {
            return MachineState.SAVE_REPORT;
        } else {
            return MachineState.INVALID_INPUT_7;
        }
    }
}
