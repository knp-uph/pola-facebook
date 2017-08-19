package com.polafacebook.engine;

import com.polafacebook.engine.query.PlatformQuery;

/**
 * Created by Piotr on 07.08.2017.
 */
public abstract class AbstractEngine {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext(Context context) {
        return context;
    }

    public abstract EngineResponse getResponse(PlatformQuery query);
}
