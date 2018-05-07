package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.BotResponses;
import com.polafacebook.model.Context;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;
import com.polafacebook.process.service.polapi.Pola;

import java.io.IOException;

public class SuggestionController {
    private final OnNewOutgoingMessageListener listener;
    private final Pola polaService;

    public SuggestionController(OnNewOutgoingMessageListener listener, Pola polaService) {
        this.listener = listener;
        this.polaService = polaService;
    }

    public MachineState onAskForChangesOrAction(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.SuggestionController.onAskForChangesOrAction.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onAskForChangesOrAction.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onAskForChangesOrAction.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.SuggestionController.onInvalidInput1.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onInvalidInput1.quickReplyNo, "NEGATIVE");
        outgoingMessage.addQuickReply(BotResponses.SuggestionController.onInvalidInput1.quickReplyYes, "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onInvalidInput2.text, context.userId));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onAffirmative.text, context.userId));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        try {
            polaService.createReport()
                    .setProductId(context.result.getProductId())
                    .setDescription(context.lastText)
                    .send();
            listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onText.text, context.userId));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage(BotResponses.SuggestionController.onText.error, context.userId));
        }
        return MachineState.WELCOME;
    }
}
