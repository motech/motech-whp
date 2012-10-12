package org.motechproject.whp.refdata.domain;

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
}

