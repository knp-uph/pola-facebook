package com.domain.ports.outgoing.communicator;

public class QuickReply {
    public final String title;
    public final String value;

    public QuickReply(String title, String value) {
        this.title = title;
        this.value = value;
    }

    @Override
    public String toString() {
        return "QuickReply{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
