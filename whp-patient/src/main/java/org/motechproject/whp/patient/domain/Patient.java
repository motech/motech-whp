package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeDiscriminator("doc.type == 'Patient'")
public class Patient extends MotechBaseDataObject {
    @JsonProperty
    private String patientId;
    @JsonProperty
    private String caseType;
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String category;
    @JsonProperty
    private String gender;
    @JsonProperty
    private String tbId;

    public Patient(){

    }

    public Patient(String patientId, String providerId, String category, String tbID, String firstName, String lastName, String gender) {
        this.patientId = patientId;
        this.providerId = providerId;
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.tbId = tbID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getTbId() {
        return tbId;
    }

    public void setTbId(String tbId) {
        this.tbId = tbId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
