package org.motechproject.whp.common.domain;

import java.util.ArrayList;
import java.util.List;

public enum SmearTestResult {

    Positive("Positive"), Negative("Negative"), Indeterminate("Indeterminate / Spoiled / Poor");

    String value;

    private SmearTestResult(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public SmearTestResult cumulativeResult(SmearTestResult smear_test_result_2) {
        if (this == Positive || smear_test_result_2 == Positive)
            return Positive;
        if (this == Indeterminate || smear_test_result_2 == Indeterminate)
            return Indeterminate;
        return Negative;
    }

    public static List<String> allNames() {
        List<String> names = new ArrayList<>();
        for(SmearTestResult result : SmearTestResult.values()){
            names.add(result.name());
        }
        return names;
    }
}

