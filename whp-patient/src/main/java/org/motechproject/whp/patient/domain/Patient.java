package org.motechproject.whp.patient.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'Patient'")
public class Patient extends MotechBaseDataObject {

    @Getter
    @Setter
    private String patientId;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private Gender gender;
    @Getter
    @Setter
    private PatientType patientType;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    private PatientStatus status = PatientStatus.Open;
    @Getter
    @Setter
    private DateTime lastModifiedDate;

    @Getter
    @Setter
    private List<ProvidedTreatment> providedTreatments = new ArrayList<ProvidedTreatment>();

    public Patient() {
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, PatientType patientType, String phoneNumber) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.patientType = patientType;
        this.phoneNumber = phoneNumber;
    }

    public void addProvidedTreatment(ProvidedTreatment providedTreatment) {
        providedTreatments.add(providedTreatment);
    }

    @JsonIgnore
    public ProvidedTreatment getLatestProvidedTreatment() {
        return providedTreatments.get(providedTreatments.size() - 1);
    }

}
