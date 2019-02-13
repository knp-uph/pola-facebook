package com.domain.process.engine.machine;

public class Transition {
    private final MachineState stateFrom;
    private final MachineState stateTo;

    public Transition(MachineState stateFrom, MachineState stateTo) {
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transition that = (Transition) o;

        if (stateFrom != that.stateFrom) return false;
        return stateTo == that.stateTo;
    }

    @Override
    public int hashCode() {
        int result = stateFrom != null ? stateFrom.hashCode() : 0;
        result = 31 * result + (stateTo != null ? stateTo.hashCode() : 0);
        return result;
    }
}
