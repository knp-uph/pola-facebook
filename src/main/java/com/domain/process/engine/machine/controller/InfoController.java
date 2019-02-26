package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.communicator.OutgoingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.process.engine.machine.MachineState;

public class InfoController {
    private final OnNewOutgoingMessageListener listener;

    public InfoController(OnNewOutgoingMessageListener listener) {
        this.listener = listener;
    }

    public MachineState onWelcome(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onWelcome.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInfo(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onInfo.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onHelp(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onHelp.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onMethodology(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.InfoController.onMethodology.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInvalidInput(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.genericInvalidInputText, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

}
