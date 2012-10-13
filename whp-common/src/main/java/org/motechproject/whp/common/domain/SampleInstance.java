package org.motechproject.whp.common.domain;

public enum SampleInstance {
    PreTreatment("Pre-Treatment"), EndIP("End of Intensive Phase"), ExtendedIP("Extended IP"), TwoMonthsIntoCP("Two Months Into CP"), EndTreatment("End Treatment");

    private String value;

    SampleInstance(String value) {
       this.value = value;
    }

    public String value(){
        return value;
    }

}

