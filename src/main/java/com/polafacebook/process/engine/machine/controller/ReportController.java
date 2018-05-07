package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.BotResponses;
import com.polafacebook.model.Context;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;
import com.polafacebook.process.engine.message.attachment.Attachment;
import com.polafacebook.process.service.polapi.Pola;

import java.io.IOException;

public class ReportController {
    private final OnNewOutgoingMessageListener listener;
    private final Pola polaService;

    public ReportController(OnNewOutgoingMessageListener listener, Pola polaService) {
        this.listener = listener;
        this.polaService = polaService;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onInvalidInput1.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput1.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput1.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onInvalidInput2.text, context.userId));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onInvalidInput3(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onInvalidInput3.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.ReportController.submitQuickReply, "SUBMIT");
        outgoingMessage.addQuickReply(BotResponses.ReportController.cancelQuickReply, "CANCEL");

        listener.onNewMessage(outgoingMessage);

        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onSubmit(MachineState from, MachineState to, Context context) {
        try {
            Pola.ReportBuilder polaReport = polaService.createReport()
                    .setProductId(context.result.getProductId())
                    .setDescription(context.description)
                    .setMimeType("image/jpg")
                    .setFileExtension("jpg");
            for (Attachment attachment : context.attachments) {
                //TODO: refactor into hexagonal architecture
                polaReport.addFileFromUrl(attachment.getUri().toURL());
            }
            polaReport.send();

            listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onSubmit.text, context.userId));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onSubmit.error, context.userId));
        } finally {
            context.attachments.clear();
        }
        return MachineState.WELCOME;
    }

    public MachineState onCancel(MachineState from, MachineState to, Context context) {
        context.attachments.clear();

        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onCancel.text, context.userId));
        return MachineState.WELCOME;
    }

    public MachineState onImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onImage.text + context.attachments.size(), context.userId);

        outgoingMessage.addQuickReply(BotResponses.ReportController.submitQuickReply, "SUBMIT");
        outgoingMessage.addQuickReply(BotResponses.ReportController.cancelQuickReply, "CANCEL");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onAffirmative.text, context.userId));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        context.description = context.lastText;
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onText.text, context.userId));
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onReportPromptImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onReportPromptImage.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.ReportController.onReportPromptImage.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onReportPromptImage.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }
}
