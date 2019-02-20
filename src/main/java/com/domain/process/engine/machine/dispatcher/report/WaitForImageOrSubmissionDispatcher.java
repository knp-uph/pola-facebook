package com.domain.process.engine.machine.dispatcher.report;

import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;

public class WaitForImageOrSubmissionDispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForImageOrSubmissionDispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        if (message.hasAttachments()) {
            //TODO: check if there can be multiple attachments at any given point
            context.getAttachments().add(message.getAttachment(0));
            return MachineState.ADD_IMAGE;
        }
        if (dispatcherHelper.isSubmit(message)) {
            return MachineState.SAVE_REPORT;
        }
        if (dispatcherHelper.isCancel(message)) {
            return MachineState.CANCEL_REPORT;
        }
        return MachineState.INVALID_INPUT_3;
    }
}
