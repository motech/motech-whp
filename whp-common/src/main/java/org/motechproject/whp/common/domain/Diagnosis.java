package org.motechproject.whp.common.domain;

import java.util.ArrayList;
import java.util.List;

public enum Diagnosis {
    Positive, Negative, Pending;

    public static List<String> allNames() {
        List<String> names = new ArrayList<>();
        for (Diagnosis diagnosis : values()) {
            names.add(diagnosis.name());
        }
        return names;
    }
}
