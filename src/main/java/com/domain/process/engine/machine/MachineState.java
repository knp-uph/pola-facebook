package com.domain.process.engine.machine;

public enum MachineState {
    WELCOME,
    HELP,
    INFO,
    METHODOLOGY,
    WAIT_FOR_ACTION (true),
    NOT_RECOGNIZED,
    PROCESS_IMAGE,
    REPORT_PROMPT_IMAGE,
    DISPLAY_RESULTS,
    WAIT_FOR_DECISION_OR_ACTION_1 (true),
    ASK_FOR_TEXT_1,
    WAIT_FOR_TEXT_1 (true),
    ASK_FOR_ADDITIONAL_IMAGES,
    WAIT_FOR_DECISION(true),
    ASK_FOR_IMAGE,
    ADD_IMAGE,
    WAIT_FOR_IMAGE_OR_SUBMISSION(true),
    CANCEL_REPORT,
    ASK_FOR_CHANGES_OR_ACTION,
    WAIT_FOR_DECISION_OR_ACTION_2 (true),
    ASK_FOR_TEXT_2,
    WAIT_FOR_TEXT_2 (true),
    SAVE_SUGGESTION,
    SAVE_REPORT,
    INVALID_INPUT_1,
    INVALID_INPUT_2,
    INVALID_INPUT_3,
    INVALID_INPUT_4,
    INVALID_INPUT_5,
    INVALID_INPUT_6,
    INVALID_INPUT_7,
    INIT,
    PROCESS_CODE;

    public final boolean blocking;

    MachineState(boolean blocking) {
        this.blocking = blocking;
    }

    MachineState() {
        this.blocking = false;
    }
}
