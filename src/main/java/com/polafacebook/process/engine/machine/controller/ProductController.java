package com.polafacebook.process.engine.machine.controller;

import com.polafacebook.BotResponses;
import com.polafacebook.model.Context;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.Action;
import com.polafacebook.process.engine.message.OutgoingMessage;
import com.polafacebook.process.service.BarCodeService;
import com.polafacebook.process.service.polapi.Pola;
import com.polafacebook.process.service.polapi.Result;

import java.io.IOException;

public class ProductController {
    public static final char CHR_CHECK_MARK = (char) 0x2713;
    public static final char CHR_EX_MARK = (char) 0x2717;

    private final OnNewOutgoingMessageListener listener;
    private BarCodeService barCodeService;
    private Pola polaService;

    public ProductController(OnNewOutgoingMessageListener listener, BarCodeService barCodeService, Pola polaService) {
        this.listener = listener;
        this.barCodeService = barCodeService;
        this.polaService = polaService;
    }

    public void setBarCodeService(BarCodeService barCodeService) {
        this.barCodeService = barCodeService;
    }

    public void setPolaService(Pola polaService) {
        this.polaService = polaService;
    }

    public MachineState onImage(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(Action.TYPING_ON, context.userId));
        String code;
        //retrieve the code from the barcode image:
        try {
            code = barCodeService.processBarCode(context.lastAttachment.getInputStream());
            if (code == null) {
                return MachineState.NOT_RECOGNIZED;
            }
            listener.onNewMessage(new OutgoingMessage(BotResponses.ProductController.onImage.text + code, context.userId));
        } catch (IOException e) {
            e.printStackTrace();
            return MachineState.NOT_RECOGNIZED;
        }
        //query the database for details on the product:
        listener.onNewMessage(new OutgoingMessage(Action.TYPING_ON, context.userId));
        return queryDB(code, context);
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ProductController.onText.text + context.eanCode, context.userId));
        return queryDB(context.eanCode, context);
    }

    public MachineState onNotRecognized(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ProductController.onNotRecognized.text, context.userId);

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onDisplayResults(MachineState from, MachineState to, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(BotResponses.ProductController.onDisplayResults.ratingText).append(context.result.getPlScore()).append("/100").append("\n");
        sb.append(BotResponses.ProductController.onDisplayResults.manufacturerText).append(context.result.getName()).append("\n");
        sb.append(BotResponses.ProductController.onDisplayResults.polishCapitaltext).append(context.result.getPlCapital()).append("%\n");
        sb.append(context.result.isPlWorkers() ? CHR_CHECK_MARK : CHR_EX_MARK).append(" " + BotResponses.ProductController.onDisplayResults.manufacturesInPolandText + "\n");
        sb.append(context.result.isPlRnD() ? CHR_CHECK_MARK : CHR_EX_MARK).append(" " +  BotResponses.ProductController.onDisplayResults.hasRndInPolandText + "\n");
        sb.append(context.result.isPlRegistered() ? CHR_CHECK_MARK : CHR_EX_MARK).append(" " + BotResponses.ProductController.onDisplayResults.isRegisteredInPolandText + "\n");
        sb.append(context.result.isPlNotGlobEnt() ? CHR_CHECK_MARK : CHR_EX_MARK).append(" " + BotResponses.ProductController.onDisplayResults.isPartOfForeignCorporationText + "\n");

        sb.append(context.result.getDescription());

        listener.onNewMessage(new OutgoingMessage(sb.toString(), context.userId));
        return MachineState.ASK_FOR_CHANGES_OR_ACTION;
    }

    private MachineState handleDBError(Context context) {
        listener.onNewMessage(new OutgoingMessage(BotResponses.ProductController.handleDBError.text, context.userId));
        return MachineState.WELCOME;
    }

    private MachineState queryDB(String code, Context context) {
        Result result;
        try {
            result = polaService.getByCode(code);
        } catch (IOException e) {
            return handleDBError(context);
        }

        context.result = result;

        if (result.getDescription() != null) {
            return MachineState.DISPLAY_RESULTS;
        } else {
            return MachineState.REPORT_PROMPT_IMAGE;
        }
    }
}
