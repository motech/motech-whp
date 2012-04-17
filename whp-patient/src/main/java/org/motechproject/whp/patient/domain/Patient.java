package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'Patient'")
public class Patient extends MotechBaseDataObject {

    @JsonProperty
    private String patientId;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private Gender gender;
    @JsonProperty
    private PatientType patientType;

    @JsonProperty
    private SmearTestResult smearTestResult;

    List<Treatment> treatments = new ArrayList<Treatment>();

    public Patient(){
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, PatientType patientType) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.patientType = patientType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public SmearTestResult getSmearTestResult() {
        return smearTestResult;
    }

    public void setSmearTestResult(SmearTestResult smearTestResult) {
        this.smearTestResult = smearTestResult;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }
}
