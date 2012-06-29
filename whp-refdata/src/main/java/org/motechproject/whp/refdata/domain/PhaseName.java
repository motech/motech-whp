package org.motechproject.whp.refdata.domain;

public enum PhaseName {
    IP("Intensive Phase"), CP("Continuation Phase"), EIP("Extended Intensive Phase");

    private String name;

    PhaseName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
