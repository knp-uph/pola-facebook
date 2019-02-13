package com.domain.process.engine.machine;

import com.domain.ports.outgoing.context.Context;

public interface TransitionListener {
    MachineState onTransition(MachineState from, MachineState to, Context context);
}
