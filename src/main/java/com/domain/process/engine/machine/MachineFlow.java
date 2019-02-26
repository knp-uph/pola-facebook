package com.domain.process.engine.machine;

public interface MachineFlow {
    void addTransition(MachineState from, MachineState to, TransitionListener listener);

    TransitionListener getTransition(MachineState from, MachineState to);

    boolean containsTransition(MachineState from, MachineState to);
}
