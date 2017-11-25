package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.model.Context;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.IncomingMessage;

public class WaitForImageOrSubmissionDispatcher implements StateDispatcher {
    private final DispatcherHelper dispatcherHelper;

    public WaitForImageOrSubmissionDispatcher(DispatcherHelper dispatcherHelper) {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public MachineState dispatch(Context context, IncomingMessage message) {
        if (message.hasAttachments()) {
            //TODO: check if there can be multiple attachments at any given point
            context.attachments.add(message.getAttachment(0));
            return MachineState.ADD_IMAGE;
        }
        if (dispatcherHelper.isSubmission(message)) {
            return MachineState.SAVE_REPORT;
        } if (dispatcherHelper.isCancellation(message)){
            return MachineState.CANCEL_REPORT;
        }
        return MachineState.INVALID_INPUT_3;
    }
}
