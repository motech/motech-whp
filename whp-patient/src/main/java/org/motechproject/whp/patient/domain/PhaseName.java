package org.motechproject.whp.patient.domain;

public enum PhaseName{
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
