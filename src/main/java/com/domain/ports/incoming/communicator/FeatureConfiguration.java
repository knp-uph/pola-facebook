package com.domain.ports.incoming.communicator;

public class FeatureConfiguration {
    private String initializationPayload;
    private String greetingText;

    public FeatureConfiguration(String initializationPayload, String greetingText) {
        this.initializationPayload = initializationPayload;
        this.greetingText = greetingText;
    }

    public String getInitializationPayload() {
        return initializationPayload;
    }

    public String getGreetingText() {
        return greetingText;
    }
}
