package org.motechproject.whp.patient.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'Patient'")
@Data
public class Patient extends MotechBaseDataObject {

    private String patientId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private PatientType patientType;
    private String phoneNumber;
    private String phi;
    private PatientStatus status = PatientStatus.Open;
    private List<ProvidedTreatment> providedTreatments = new ArrayList<ProvidedTreatment>();
    private DateTime lastModifiedDate;

    @Getter
    @Setter
    private ProvidedTreatment currentProvidedTreatment;

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
        if (currentProvidedTreatment != null) {
            providedTreatments.add(currentProvidedTreatment);
        }
        currentProvidedTreatment = providedTreatment;
    }

    public ProvidedTreatment latestProvidedTreatment() {
        return currentProvidedTreatment;
    }

    @JsonIgnore
    public boolean isValid() {
        return currentProvidedTreatment.isValid();
    }
}
