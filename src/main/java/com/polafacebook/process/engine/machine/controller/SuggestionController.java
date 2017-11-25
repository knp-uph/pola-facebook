package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.model.Context;
import com.polafacebook.polapi.Pola;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;

import java.io.IOException;

public class SuggestionController {
    private final OnNewOutgoingMessageListener listener;
    private final Pola polaService;

    public SuggestionController(OnNewOutgoingMessageListener listener, Pola polaService) {
        this.listener = listener;
        this.polaService = polaService;
    }

    public MachineState onAskForChangesOrAction(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Czy chcesz zgłosić poprawkę do przedstawionych informacji?", context.userId);

        outgoingMessage.addQuickReply("Nie. Jest okej.", "NEGATIVE");
        outgoingMessage.addQuickReply("Tak. Chcę zgłosić.", "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Nie rozumiem. Tak czy nie? A może chcesz podać kolejny kod i wrócić do początku?", context.userId);

        outgoingMessage.addQuickReply("Nie. Wróć.", "NEGATIVE");
        outgoingMessage.addQuickReply("Tak. Chcę zgłosić.", "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_2;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage("Nie rozumiem. Może po prostu mi coś napisz?", context.userId));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage("Super! Powiedz mi proszę, co się nie zgadza lub czego brakuje!", context.userId));
        return MachineState.WAIT_FOR_TEXT_2;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        try {
            polaService.createReport()
                    .setProductId(context.result.getProductId())
                    .setDescription(context.lastText)
                    .send();
            listener.onNewMessage(new OutgoingMessage("Dziękuję. Wysłałem sugestię. :P", context.userId));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage("Ups! Mamy usterkę, nie udało mi się wysłać sugestii. Może spróbuj później?", context.userId));
        }
        return MachineState.WELCOME;
    }
}
