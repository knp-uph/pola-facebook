package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.ports.outgoing.productinformation.ReportBuilder;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.message.OutgoingMessage;
import com.domain.process.engine.message.attachment.Attachment;

import java.io.IOException;

public class ReportController {
    private final OnNewOutgoingMessageListener listener;
    private final ProductInformationService productInformationService;

    public ReportController(OnNewOutgoingMessageListener listener, ProductInformationService productInformationService) {
        this.listener = listener;
        this.productInformationService = productInformationService;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onInvalidInput1.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput1.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput1.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onInvalidInput2.text, context.getUserId()));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onInvalidInput3(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onInvalidInput3.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.submitQuickReply, "SUBMIT");
        outgoingMessage.addQuickReply(BotResponses.ReportController.cancelQuickReply, "CANCEL");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onInvalidInput4(MachineState machineState, MachineState machineState1, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onInvalidInput4.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput4.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onInvalidInput4.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION;
    }

    public MachineState onSubmit(MachineState from, MachineState to, Context context) {
        try {
            ReportBuilder reportBuilder = productInformationService.createReport()
                    .setProductId(context.getProductInformation().getProductId())
                    .setDescription(context.getDescription())
                    .setMimeType("image/jpg")
                    .setFileExtension("jpg");
            for (Attachment attachment : context.getAttachments()) {
                reportBuilder.addFileFromUrl(attachment.getUri().toURL());
            }
            reportBuilder.send();

            listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onSubmit.text, context.getUserId()));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onSubmit.error, context.getUserId()));
        } finally {
            context.getAttachments().clear();
        }
        return MachineState.WELCOME;
    }

    public MachineState onCancel(MachineState from, MachineState to, Context context) {
        context.getAttachments().clear();

        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onCancel.text, context.getUserId()));
        return MachineState.WELCOME;
    }

    public MachineState onImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onImage.text + context.getAttachments().size(), context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.submitQuickReply, "SUBMIT");
        outgoingMessage.addQuickReply(BotResponses.ReportController.cancelQuickReply, "CANCEL");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onAffirmative.text, context.getUserId()));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        context.setDescription(context.getLastText());

        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onAskForAddtionalImages.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.onAskForAddtionalImages.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onAskForAddtionalImages.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION;
    }

    public MachineState onReportPromptImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ReportController.onReportPromptImage.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.ReportController.onReportPromptImage.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.ReportController.onReportPromptImage.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }

    public MachineState onAdditionalImagesConfirmed(MachineState machineState, MachineState machineState1, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ReportController.onText.text, context.getUserId()));
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }
}
