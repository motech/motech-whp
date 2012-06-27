package org.motechproject.whp.functional.data;


import lombok.Data;

@Data
public class TestPatient {

    private String caseId;
    private String providerId;
    private String firstName;
    private String village;

    public TestPatient(String caseId, String providerId, String firstName, String village) {
        this.caseId = caseId;
        this.providerId = providerId;
        this.firstName = firstName;
        this.village = village;
    }
}
