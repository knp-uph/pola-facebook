package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.model.Context;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.OutgoingMessage;

public class InfoController {
    private final OnNewOutgoingMessageListener listener;

    //TODO: move it somewhere else
    private String welcomeText = "Witaj w Poli! Prześlij nam zdjęcie kodu kreskowego lub wpisz kod ręcznie, a my powiemy Ci czy to polski produkt. Możesz też wybrać którąś z poniższych opcji.";
    private String methodologyText = "Każdemu producentowi Pola przypisuje od 0 do 100 punktów. Pierwsze 35 punktów przyznaje proporcjonalnie do udziału polskiego kapitału w konkretnym przedsiębiorstwie. Dalsze 10 punktów otrzymuje ta firma, która jest zarejestrowana w Polsce, a kolejne 30, o ile produkuje w naszym kraju. Dalsze 15 punktów zależy od tego, czy zatrudnia w naszym kraju w obszarze badań i rozwoju. Wreszcie ostatnie 10 punktów otrzymują firmy, które nie są częścią zagranicznych koncernów.";
    private String informationText = "Masz dość masówki globalnych koncernów? Szukasz lokalnych firm tworzących unikatowe produkty? Pola pomoże Ci odnaleźć polskie wyroby. Zabierając Polę na zakupy, odnajdujesz produkty „z duszą” i wspierasz polską gospodarkę.\n" +
            "Zeskanuj kod kreskowy z dowolnego produktu i dowiedz się więcej o firmie, która go wyprodukowała. Zeskanuj kod kreskowy z dowolnego produktu i dowiedz się więcej o firmie, która go wyprodukowała. Pola powie Ci, ile dany producent posiada polskiego kapitału, czy w Polsce ulokował swoją produkcję, tworzy wykwalifikowane miejsca pracy w dziale badań i rozwoju, w końcu czy zarejestrował swoją działalność na terenie naszego kraju oraz czy jest częścią zagranicznego koncernu.\n" +
            "Jeśli znajdziesz firmę, której nie ma w naszej bazie, koniecznie zgłoś ją do nas. Pomożesz nam w ten sposób uzupełniać unikatową bazę polskich producentów. Więcej informacji na www.pola-app.pl/";

    private String helpText = "Aby uzyskać informacje o producencie danego produktu, wystarczy wysłać nam zdjęcie z kodem kreskowym lub wpisać kod ręcznie. Możesz ponadto zapytać się o metodykę czy informacje o usłudze.";

    public InfoController(OnNewOutgoingMessageListener listener) {
        this.listener = listener;
    }

    public MachineState onWelcome(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(welcomeText, context.userId);

        outgoingMessage.addQuickReply("Pomoc", "HELP");
        outgoingMessage.addQuickReply("Informacje", "INFO");
        outgoingMessage.addQuickReply("Metodyka", "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInfo(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(informationText, context.userId);

        outgoingMessage.addQuickReply("Pomoc", "HELP");
        outgoingMessage.addQuickReply("Metodyka", "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onHelp(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(helpText, context.userId);

        outgoingMessage.addQuickReply("Informacje", "INFO");
        outgoingMessage.addQuickReply("Metodyka", "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onMethodology(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(methodologyText, context.userId);

        outgoingMessage.addQuickReply("Informacje", "INFO");
        outgoingMessage.addQuickReply("Pomoc", "HELP");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onInvalidInput(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage("Nie rozumiem. :/", context.userId);

        outgoingMessage.addQuickReply("Pomoc", "HELP");
        outgoingMessage.addQuickReply("Informacje", "INFO");
        outgoingMessage.addQuickReply("Metodyka", "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

}
