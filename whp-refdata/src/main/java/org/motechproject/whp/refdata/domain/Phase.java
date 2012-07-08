package org.motechproject.whp.refdata.domain;

public enum Phase {

    IP("Intensive Phase"), CP("Continuation Phase"), EIP("Extended Intensive Phase");

    private String name;

    Phase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
