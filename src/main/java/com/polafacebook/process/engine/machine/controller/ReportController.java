package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.model.Context;
import com.polafacebook.polapi.Pola;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;
import com.polafacebook.process.engine.message.attachment.Attachment;

import java.io.IOException;

public class ReportController {
    private final OnNewOutgoingMessageListener listener;
    private final Pola polaService;

    public ReportController(OnNewOutgoingMessageListener listener, Pola polaService) {
        this.listener = listener;
        this.polaService = polaService;
    }

    public MachineState onInvalidInput1(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Nie rozumiem. Tak czy nie? A może chcesz podać kolejny kod i wrócić do początku?", context.userId);

        outgoingMessage.addQuickReply("Nie zgłaszam. Wróć.", "NEGATIVE");
        outgoingMessage.addQuickReply("Tak, zgłaszam brak.", "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }

    public MachineState onInvalidInput2(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage("Nie rozumiem. Może po prostu mi coś napisz?", context.userId));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onInvalidInput3(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Nie rozumiem. :/ Jeśli chcesz zakończyć wypełniane raportu, to napisz 'ok' albo 'gotowe'. Możesz też go anulować lub dodać kolejne zdjęcie.", context.userId);

        outgoingMessage.addQuickReply("Gotowe.", "SUBMIT");
        outgoingMessage.addQuickReply("Anuluj.", "CANCEL");

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
                polaReport.addFile(attachment.getInputStream());
            }
            polaReport.send();

            listener.onNewMessage(new OutgoingMessage("Dziękuję. Wysłałem raport. :P", context.userId));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNewMessage(new OutgoingMessage("Ups! Mamy usterkę, nie udało mi się wysłać raportu. Może spróbuj później?", context.userId));
        } finally {
            context.attachments.clear();
        }
        return MachineState.WELCOME;
    }

    public MachineState onCancel(MachineState from, MachineState to, Context context) {
        context.attachments.clear();

        listener.onNewMessage(new OutgoingMessage("Spoko, raport anulowany. Dzięki!", context.userId));
        return MachineState.WELCOME;
    }

    public MachineState onImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Przyjąłem! Obrazków łącznie: " + context.attachments.size(), context.userId);

        outgoingMessage.addQuickReply("Gotowe.", "SUBMIT");
        outgoingMessage.addQuickReply("Anuluj.", "CANCEL");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onAffirmative(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage("Super! Podaj nam krótki opis brakującego produktu.", context.userId));
        return MachineState.WAIT_FOR_TEXT_1;
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        context.description = context.lastText;
        listener.onNewMessage(new OutgoingMessage("Super! Prześlij nam teraz jakieś fotki produktu do raportu. ;)", context.userId));
        return MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION;
    }

    public MachineState onReportPromptImage(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Nie znaleźliśmy tego produktu w naszej bazie danych. Może chcesz zgłosić ten brak?", context.userId);

        outgoingMessage.addQuickReply("Nie. Jest okej.", "NEGATIVE");
        outgoingMessage.addQuickReply("Tak. Chcę zgłosić.", "AFFIRMATIVE");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_DECISION_OR_ACTION_1;
    }
}
