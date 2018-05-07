package com.polafacebook.model;

import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.message.attachment.Attachment;
import com.polafacebook.process.service.polapi.Result;

import java.util.ArrayList;

public class Context {
    final public String userId;
    //public Message lastMessage;
    public String description;
    public MachineState state = MachineState.INIT;

    public ArrayList<Attachment> attachments = new ArrayList<>();
    public String eanCode;
    public Result result;

    public String lastText;
    public Attachment lastAttachment;

    public Context(String id) {
        this.userId = id;
    }

    public Context(String id, MachineState state) {
        this.userId = id;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Context{" +
                "userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", attachments=" + attachments +
                ", eanCode='" + eanCode + '\'' +
                ", result=" + result +
                ", lastText='" + lastText + '\'' +
                ", lastAttachment=" + lastAttachment +
                '}';
    }
}
