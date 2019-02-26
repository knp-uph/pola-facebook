package com.domain.ports.dto;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public abstract class Attachment {

    public enum Type {
        IMAGE,
    }
    public Type type;

    public Attachment() {
    }

    protected Attachment(Type type) {
        this.type = type;
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract URI getUri();
}
