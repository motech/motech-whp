package org.motechproject.whp.adherence.domain;

import static java.lang.Integer.parseInt;

public enum PillStatus {

    Taken(1), NotTaken(2), Unknown(0);

    private int status;

    PillStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static PillStatus get(int status) {
        switch (status) {
            case 0:
                return Unknown;
            case 1:
                return Taken;
            case 2:
                return NotTaken;
            default:
                return null;
        }
    }

    public static PillStatus getFromIVRInput(int input) {
        switch (input) {
            case 1:
                return Taken;
            case 2:
                return NotTaken;
            default:
                return Unknown;
        }
    }
}

