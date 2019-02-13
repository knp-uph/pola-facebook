package com.adapters.outgoing.redis.converters;

import com.domain.ports.outgoing.productinformation.ProductInformation;

import java.util.ArrayList;

public class ContextEntity {

    public String userId;

    public String description;
    public String state;

    public ArrayList<AttachmentEntity> attachments = new ArrayList<>();
    public String eanCode;
    public ProductInformation productInformation;

    public String lastText;
    public AttachmentEntity lastAttachment;


    public ContextEntity() {
    }

    @Override
    public String toString() {
        return "ContextEntity{" +
                "userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", attachments=" + attachments +
                ", eanCode='" + eanCode + '\'' +
                ", polaResult=" + productInformation +
                ", lastText='" + lastText + '\'' +
                ", lastAttachment=" + lastAttachment +
                '}';
    }
}
