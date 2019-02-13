package com.domain.process.engine.machine.dispatcher.report;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.message.IncomingMessage;

public class WaitForDecisionOrAction1Dispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;
    private final StateDispatcher homeDispatcher;

    public WaitForDecisionOrAction1Dispatcher(DispatcherHelper dispatcherHelper, StateDispatcher homeDispatcher) {
        this.dispatcherHelper = dispatcherHelper;
        this.homeDispatcher = homeDispatcher;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        if (dispatcherHelper.isAffirmative(message)) {
            return MachineState.ASK_FOR_TEXT_1;
        }
        if (dispatcherHelper.isNegative(message)) {
            return MachineState.WELCOME;
        }

        MachineState homeDispatch = homeDispatcher.dispatch(context, message);

        if (homeDispatch == MachineState.INVALID_INPUT_1) {  //TODO: desnowflake  - special state property?
            return MachineState.INVALID_INPUT_5;
        } else {
            return homeDispatch;
        }
    }
}
