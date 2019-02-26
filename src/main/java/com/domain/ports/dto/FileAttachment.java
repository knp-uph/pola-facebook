package com.domain.ports.dto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileAttachment extends Attachment {

    private final String file;

    public FileAttachment(String file, Attachment.Type type) {
        super(type);
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public URI getUri() {
        return URI.create("file://" + file);
    }

    @Override
    public String toString() {
        return "FileAttachment{" +
                "file='" + file + '\'' +
                ", type=" + type +
                '}';
    }
}
