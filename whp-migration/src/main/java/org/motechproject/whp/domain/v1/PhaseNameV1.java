package org.motechproject.whp.domain.v1;

public enum PhaseNameV1 {
    IP("Intensive Phase"), CP("Continuation Phase"), EIP("Extended Intensive Phase");

    private String name;

    PhaseNameV1(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
