package com.polafacebook.process.engine.message.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by Piotr on 24.09.2017.
 */
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
