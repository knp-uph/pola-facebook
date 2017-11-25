package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

public class WaitForText1Dispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForText1Dispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        String text = message.getText().toUpperCase();
        if (text.length() != 0) {
            return MachineState.ASK_FOR_IMAGE;
        }
        return MachineState.INVALID_INPUT_2;
    }
}
