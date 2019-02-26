package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.communicator.OutgoingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.process.engine.machine.MachineState;

import java.io.IOException;

public class SuggestionController {
    private final OnNewOutgoingMessageListener listener;
    private final ProductInformationService productInformationService;

    public SuggestionController(OnNewOutgoingMessageListener listener, ProductInformationService productInformationService) {
        this.listener = listener;
        this.productInformationService = productInformationService;
    }

    public MachineState onAskForChangesOrAction(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.SuggestionController.onAskForChangesOrAction.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onAskForChangesOrAction.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onAskForChangesOrAction.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.SuggestionController.onInvalidInput1.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onInvalidInput1.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onInvalidInput1.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onInvalidInput2.text, context.getUserId()));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onAffirmative.text, context.getUserId()));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        try {
            productInformationService.createReport()
                    .setProductId(context.getProductInformation().getProductId())
                    .setDescription(context.getLastText())
                    .send();
            listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onText.text, context.getUserId()));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onText.error, context.getUserId()));
        }
        return MachineState.WELCOME;
    }
}
