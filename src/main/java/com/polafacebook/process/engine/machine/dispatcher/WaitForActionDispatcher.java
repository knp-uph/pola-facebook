package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

public class WaitForActionDispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForActionDispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        String text = message.getText().toUpperCase();
        if (text.contains("METODYKA") || text.contains("METODOLOGIA")) {
            return MachineState.METHODOLOGY;
        }
        if (text.contains("INFO")) {
            return MachineState.INFO;
        }
        if (text.contains("POMOC")) {
            return MachineState.HELP;
        }
        if (message.hasAttachments()) {
            return MachineState.PROCESS_IMAGE;
        }
        //TODO: multi-code support?
        String[] codes = dispatcherHelper.extractCode(message);
        if (codes.length > 0) {
            context.eanCode = codes[0];
            return MachineState.PROCESS_CODE;
        }

        return MachineState.INVALID_INPUT_1;
    }
}
