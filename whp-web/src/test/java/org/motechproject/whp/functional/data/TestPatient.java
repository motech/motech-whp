package org.motechproject.whp.functional.data;


import lombok.Data;

@Data
public class TestPatient {

    private String caseId;
    private String tbId;
    private String providerId;
    private String firstName;
    private String village;
    private String diseaseClass;

    public TestPatient(String caseId, String tbId, String providerId, String firstName) {
        this.caseId = caseId;
        this.tbId = tbId;
        this.providerId = providerId;
        this.firstName = firstName;
        this.village = "village";
        this.diseaseClass = "P";
    }
}
