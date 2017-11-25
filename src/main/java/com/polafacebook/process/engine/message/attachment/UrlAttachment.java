package com.polafacebook.process.engine.message.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlAttachment extends Attachment {

    public URL url;

    public UrlAttachment() {
        super();
    }

    public UrlAttachment(Type type, URL url) {
        super(type);
        this.url = url;
    }

    public UrlAttachment(Type type, String url) throws MalformedURLException {
        super(type);
        this.url = new URL(url);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        return httpConn.getInputStream();
    }

    @Override
    public String toString() {
        return "UrlAttachment{" +
                "url=" + url +
                '}';
    }
}
