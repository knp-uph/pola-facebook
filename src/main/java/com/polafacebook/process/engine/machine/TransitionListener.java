package com.polafacebook.process.engine.machine;

import com.polafacebook.model.Context;

public interface TransitionListener {
    MachineState onTransition(MachineState from, MachineState to, Context context);
}
