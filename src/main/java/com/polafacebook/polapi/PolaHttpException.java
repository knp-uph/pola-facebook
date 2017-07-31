package com.polafacebook.polapi;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Piotr on 21.07.2017.
 */
public class PolaHttpException extends IOException {
    public PolaHttpException(URL url, int httpResponseCode) {
        super("Server returned HTTP response code: " + httpResponseCode + " for URL: " + url.getProtocol() + "://" + url.getHost() + url.getPath());
    }
}
