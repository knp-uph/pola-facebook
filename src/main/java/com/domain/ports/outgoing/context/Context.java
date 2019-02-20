package com.domain.ports.outgoing.context;

import com.domain.ports.outgoing.productinformation.ProductInformation;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.message.attachment.Attachment;

import java.util.ArrayList;

public class Context {
    private final String userId;

    private String description;
    private MachineState state = MachineState.INIT;

    private ArrayList<Attachment> attachments = new ArrayList<>();
    private String eanCode;
    private ProductInformation productInformation;

    private String lastText;
    private Attachment lastAttachment;

    public Context(String id) {
        this.userId = id;
    }

    public Context(String id, MachineState state) {
        this.userId = id;
        this.setState(state);
    }

    /**
     * Use this to purge non-vital information in context object. Only userId and state are preserved.
     */
    public void clear() {
        description = null;
        attachments = new ArrayList<>();
        eanCode = null;
        productInformation = null;
        lastText = null;
        lastAttachment = null;
    }

    @Override
    public String toString() {
        return "Context{" +
                "userId='" + getUserId() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", state=" + getState() +
                ", attachments=" + getAttachments() +
                ", eanCode='" + getEanCode() + '\'' +
                ", polaResult=" + getProductInformation() +
                ", lastText='" + getLastText() + '\'' +
                ", lastAttachment=" + getLastAttachment() +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MachineState getState() {
        return state;
    }

    public void setState(MachineState state) {
        this.state = state;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public ProductInformation getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(ProductInformation productInformation) {
        this.productInformation = productInformation;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public Attachment getLastAttachment() {
        return lastAttachment;
    }

    public void setLastAttachment(Attachment lastAttachment) {
        this.lastAttachment = lastAttachment;
    }
}
