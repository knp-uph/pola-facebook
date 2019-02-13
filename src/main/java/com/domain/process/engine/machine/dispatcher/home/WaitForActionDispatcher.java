package com.domain.process.engine.machine.dispatcher.home;

import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.message.IncomingMessage;

public class WaitForActionDispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForActionDispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        context = context.getCleanContext();

        String text = message.getText().toUpperCase();
        if (text.contains("METODYKA") || text.contains("METODOLOGIA")) {
            return MachineState.METHODOLOGY;
        }
        if (text.contains("INFO")) {
            return MachineState.INFO;
        }
        if (text.contains("POMOC") || text.contains("INSTRUKCJA")) {
            return MachineState.HELP;
        }
        if (message.hasAttachments()) {
            return MachineState.PROCESS_IMAGE;
        }
        //TODO: multi-code support?
        String[] codes = dispatcherHelper.extractCode(message);
        if (codes.length > 0) {
            context.setEanCode(codes[0]);
            return MachineState.PROCESS_CODE;
        }

        return MachineState.INVALID_INPUT_1;
    }
}
