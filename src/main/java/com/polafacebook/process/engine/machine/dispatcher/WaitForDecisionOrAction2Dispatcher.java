package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

public class WaitForDecisionOrAction2Dispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;
    private final StateDispatcher homeDispatcher;

    public WaitForDecisionOrAction2Dispatcher(DispatcherHelper dispatcherHelper, StateDispatcher homeDispatcher) {
        this.dispatcherHelper = dispatcherHelper;
        this.homeDispatcher = homeDispatcher;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        if (dispatcherHelper.isAffirmative(message)) {
            return MachineState.ASK_FOR_TEXT_2;
        }
        if (dispatcherHelper.isNegative(message)) {
            return MachineState.WELCOME;
        }

        MachineState homeDispatch = homeDispatcher.dispatch(context, message);

        if (homeDispatch == MachineState.INVALID_INPUT_1) {  //TODO: desnowflake  - special state property?
            return MachineState.INVALID_INPUT_6;
        } else {
            return homeDispatch;
        }
    }
}
