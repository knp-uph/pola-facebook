package com.adapters.outgoing.pola;

import java.io.IOException;

/**
 * Created by Piotr on 21.07.2017.
 */
public class PolaHttpException extends IOException {
    public PolaHttpException(String url, int httpResponseCode) {
        super("Server returned HTTP response code: " + httpResponseCode + " for URL: " + url);
    }
}
