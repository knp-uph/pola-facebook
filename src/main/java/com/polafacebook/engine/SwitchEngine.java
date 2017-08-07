package com.polafacebook.engine;

import com.polafacebook.engine.query.PlatformQuery;
import com.polafacebook.polapi.Pola;
import com.polafacebook.service.BarCodeService;

/**
 * Created by Piotr on 07.08.2017.
 */
public class SwitchEngine extends AbstractEngine {
    Pola pola = new Pola();
    private BarCodeService barCodeService;

    /**
     * Prompts the conversation engine to respond to stimuli.
     * @param query
     * @return
     */
    @Override
    public EngineResponse getResponse(PlatformQuery query) {

        return null;
    }
}
