package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

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
