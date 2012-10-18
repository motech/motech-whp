package org.motechproject.whp.functional.data;

import java.util.List;

import static java.util.Arrays.asList;

public class TestContainers {

    public static final String containerIssuedFrom = "02/10/2012";
    public static final String containerIssuedTo = "04/10/2012";

    public TestContainer containerWhichDoesNotMatchDateRange() {
        return new TestContainer()
                .setContainerId("90000000020")
                .setProviderId("providerid")
                .setInstance("Pre-treatment")
                .setDiagnosis("Positive")
                .setContainerIssuedDate("01/10/2012")
                .setConsultationDate("01/10/2012")
                .setDistrict("Begusarai")
                .setStatus("Open")
                .setLabResult("Positive");


    }

    public TestContainer containerWhichDoesNotMatchDropDownField() {
        return new TestContainer()
                .setContainerId("90000000021")
                .setProviderId("providerid")
                .setInstance("Pre-treatment")
                .setDiagnosis("Negative")
                .setContainerIssuedDate("03/10/2012")
                .setConsultationDate("01/10/2012")
                .setDistrict("Begusarai")
                .setStatus("Open")
                .setLabResult("Positive");
    }

    public TestContainer containerWhichDoesNotMatchAutoCompleteField() {
        return new TestContainer()
                .setContainerId("90000000022")
                .setProviderId("providerid")
                .setInstance("Pre-treatment")
                .setDiagnosis("Positive")
                .setContainerIssuedDate("03/10/2012")
                .setConsultationDate("01/10/2012")
                .setDistrict("Vaishali")
                .setStatus("Open")
                .setLabResult("Positive");
    }

    public TestContainer containerWhichMatchesAllCriteria() {
        return new TestContainer()
                .setContainerId("90000000023")
                .setProviderId("providerid")
                .setDiagnosis("Positive")
                .setInstance("Pre-treatment")
                .setContainerIssuedDate("03/10/2012")
                .setConsultationDate("03/10/2012")
                .setDistrict("Begusarai")
                .setStatus("Open")
                .setLabResult("Positive");
    }


    public TestContainer containerNotRegistered() {
        return new TestContainer()
                .setContainerId("90000000024")
                .setInstance("Pre-treatment")
                .setProviderId("providerid")
                .setDiagnosis("Positive")
                .setContainerIssuedDate("03/10/2012")
                .setConsultationDate("03/10/2012")
                .setDistrict("Begusarai")
                .setStatus("Open")
                .setLabResult("Positive");
    }

    public List<TestContainer> allRegisteredContainers() {
        return asList(
                containerWhichDoesNotMatchAutoCompleteField(),
                containerWhichDoesNotMatchDateRange(),
                containerWhichDoesNotMatchDropDownField(),
                containerWhichMatchesAllCriteria()
        );
    }

}
