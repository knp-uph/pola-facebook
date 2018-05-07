package com.adapters.dto;

import com.polafacebook.process.service.polapi.Result;

import java.util.ArrayList;

public class ContextEntity {

    public String userId;
    //public Message lastMessage;
    public String description;
    public String state;

    public ArrayList<AttachmentEntity> attachments = new ArrayList<>();
    public String eanCode;
    public Result result;

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
                ", result=" + result +
                ", lastText='" + lastText + '\'' +
                ", lastAttachment=" + lastAttachment +
                '}';
    }
}
