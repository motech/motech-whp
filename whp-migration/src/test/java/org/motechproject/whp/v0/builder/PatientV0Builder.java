package org.motechproject.whp.v0.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.v0.domain.*;

import java.util.Arrays;

public class PatientV0Builder {

    public static final String CASE_ID = "caseid";
    public static final String NEW_TB_ID = "newtbid";
    public static final String NEW_PROVIDER_ID = "newproviderid";
    protected final TreatmentCategory category01 = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
    protected final TreatmentCategory category10 = new TreatmentCategory("RNTCP Category 1", "10", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday));

    private final PatientV0 patientV0;

    public PatientV0Builder() {
        patientV0 = new PatientV0();
    }

    public PatientV0Builder withDefaults() {
        patientV0.setPatientId(CASE_ID);
        patientV0.setFirstName("firstName");
        patientV0.setLastName("lastName");
        patientV0.setGender(GenderV0.O);
        patientV0.setPhoneNumber("1234567890");
        patientV0.setCurrentTreatment(new TreatmentV0Builder().withDefaults().build());
        return this;
    }

    public PatientV0 build() {
        return patientV0;
    }

    public PatientV0Builder withType(PatientTypeV0 type) {
        patientV0.getCurrentTreatment().setPatientType(type);
        return this;
    }

    public PatientV0Builder withPatientId(String patientId) {
        patientV0.setPatientId(patientId);
        return this;
    }

    public PatientV0Builder onTreatmentFrom(LocalDate date) {
        patientV0.latestTherapy().setStartDate(date);
        return this;
    }

    public PatientV0Builder withTbId(String tbId) {
        patientV0.getCurrentTreatment().setTbId(tbId);
        return this;
    }

    public PatientV0Builder withCurrentTreatment(TreatmentV0 currentTreatment) {
        patientV0.setCurrentTreatment(currentTreatment);
        return this;
    }

    public PatientV0Builder withStatus(PatientStatusV0 status) {
        patientV0.setStatus(status);
        return this;
    }

    public PatientV0Builder withMigrated(boolean migrated) {
        patientV0.setMigrated(migrated);
        return this;
    }

    public PatientV0Builder withTherapyDocId(String therapyDocId) {
        patientV0.getCurrentTreatment().setTherapyDocId(null);
        patientV0.getCurrentTreatment().setTherapyDocId(therapyDocId);
        patientV0.getCurrentTreatment().getTherapy().setId(therapyDocId);
        return this;
    }

    public PatientV0Builder withCurrentTherapy(TherapyV0 therapyV0) {
        patientV0.getCurrentTreatment().setTherapy(therapyV0);
        return this;
    }

}