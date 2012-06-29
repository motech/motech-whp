package org.motechproject.whp.functional.data;


import lombok.Data;

@Data
public class TestPatient {

    private String caseId;
    private String tbId;
    private String providerId;
    private String firstName;
    private String district;
    private String village;
    private String diseaseClass;

    public TestPatient(String caseId, String tbId, String providerId, String firstName, String district) {
        this.caseId = caseId;
        this.tbId = tbId;
        this.providerId = providerId;
        this.firstName = firstName;
        this.district = district;
        this.village = "village";
        this.diseaseClass = "P";
    }

    public void transferIn(String newTbId, String providerId) {
        setTbId(newTbId);
        setProviderId(providerId);
    }

}
