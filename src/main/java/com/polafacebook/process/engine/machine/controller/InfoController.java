package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.BotResponses;
import com.polafacebook.model.Context;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;

public class InfoController {
    private final OnNewOutgoingMessageListener listener;

    public InfoController(OnNewOutgoingMessageListener listener) {
        this.listener = listener;
    }

    public MachineState onWelcome(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onWelcome.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInfo(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onInfo.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onHelp(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onHelp.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onMethodology(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onMethodology.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInvalidInput(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.genericInvalidInputText, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

}
