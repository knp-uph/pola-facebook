package com.domain.process.engine.machine.controller;

import com.domain.BotResponses;
import com.domain.ports.outgoing.communicator.Action;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.communicator.OutgoingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.productinformation.ProductInformation;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.service.BarCodeService;

import java.io.IOException;

public class ProductController {
    public static final char CHR_CHECK_MARK = (char) 0x2611;
    public static final char CHR_NO_MARK = (char) 0x2610;

    private final OnNewOutgoingMessageListener listener;
    private BarCodeService barCodeService;
    private ProductInformationService productInformationService;

    public ProductController(OnNewOutgoingMessageListener listener, BarCodeService barCodeService, ProductInformationService productInformationService) {
        this.listener = listener;
        this.barCodeService = barCodeService;
        this.productInformationService = productInformationService;
    }

    public MachineState onImage(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(Action.TYPING_ON, context.getUserId()));
        String code;
        //retrieve the code from the barcode image:
        try {
            code = barCodeService.processBarCode(context.getLastAttachment().getInputStream());
            if (code == null) {
                return MachineState.NOT_RECOGNIZED;
            }
            listener.onNewMessage(new OutgoingMessage(BotResponses.ProductController.onImage.text + code, context.getUserId()));
        } catch (IOException e) {
            e.printStackTrace();
            return MachineState.NOT_RECOGNIZED;
        }
        //query the database for details on the product:
        listener.onNewMessage(new OutgoingMessage(Action.TYPING_ON, context.getUserId()));
        return queryDB(code, context);
    }

    public MachineState onText(MachineState from, MachineState to, Context context) {
        listener.onNewMessage(new OutgoingMessage(Action.TYPING_ON, context.getUserId()));
        return queryDB(context.getEanCode(), context);
    }

    public MachineState onNotRecognized(MachineState from, MachineState to, Context context) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(BotResponses.ProductController.onNotRecognized.text, context.getUserId());

        outgoingMessage.addQuickReply(BotResponses.InfoController.helpQuickReply, "HELP");
        outgoingMessage.addQuickReply(BotResponses.InfoController.infoQuickReply, "INFO");
        outgoingMessage.addQuickReply(BotResponses.InfoController.methodologyQuickReply, "METHODOLOGY");

        listener.onNewMessage(outgoingMessage);
        return MachineState.WAIT_FOR_ACTION;
    }

    public MachineState onDisplayResults(MachineState from, MachineState to, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(BotResponses.ProductController.onDisplayResults.ratingText).append(context.getProductInformation().getRating()).append("/100").append("\n");
        sb.append(BotResponses.ProductController.onDisplayResults.manufacturerText).append(context.getProductInformation().getName()).append("\n");
        sb.append(BotResponses.ProductController.onDisplayResults.polishCapitaltext).append(context.getProductInformation().getPolishCapitalPercentage()).append("%\n");
        sb.append(context.getProductInformation().getHiresInPoland().equals(Boolean.TRUE) ? CHR_CHECK_MARK : CHR_NO_MARK).append(" " + BotResponses.ProductController.onDisplayResults.manufacturesInPolandText + "\n");
        sb.append(context.getProductInformation().getRndInPoland().equals(Boolean.TRUE) ? CHR_CHECK_MARK : CHR_NO_MARK).append(" " + BotResponses.ProductController.onDisplayResults.hasRndInPolandText + "\n");
        sb.append(context.getProductInformation().getRegisteredInPoland().equals(Boolean.TRUE) ? CHR_CHECK_MARK : CHR_NO_MARK).append(" " + BotResponses.ProductController.onDisplayResults.isRegisteredInPolandText + "\n");
        sb.append(context.getProductInformation().getPartOfGlobalCorporation().equals(Boolean.TRUE) ? CHR_CHECK_MARK : CHR_NO_MARK).append(" " + BotResponses.ProductController.onDisplayResults.isPartOfForeignCorporationText + "\n");

        sb.append(context.getProductInformation().getDescription());

        listener.onNewMessage(new OutgoingMessage(sb.toString(), context.getUserId()));
        return MachineState.ASK_FOR_CHANGES_OR_ACTION;
    }

    private MachineState queryDB(String code, Context context) {
        ProductInformation productInformation;
        try {
            productInformation = productInformationService.getByCode(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        context.setProductInformation(productInformation);

        if (productInformation != null) {
            return MachineState.DISPLAY_RESULTS;
        } else {
            context.getAttachments().add(context.getLastAttachment());
            return MachineState.REPORT_PROMPT_IMAGE;
        }
    }
}
