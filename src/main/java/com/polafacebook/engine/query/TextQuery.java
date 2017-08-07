package com.polafacebook.engine.query;

import java.util.Date;

/**
 * Created by Piotr on 07.08.2017.
 */
public class TextQuery extends PlatformQuery {
    public TextQuery() {
    }

    public TextQuery(Date timestamp, String senderId, String payload) {
        super(timestamp, senderId, payload);
    }

    public TextQuery(Date timestamp, String senderId) {
        super(timestamp, senderId);
    }
}
