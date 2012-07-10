package org.motechproject.whp.v1.mapper;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.v0.builder.PatientV0Builder;
import org.motechproject.whp.v0.builder.TherapyV0Builder;
import org.motechproject.whp.v0.builder.TreatmentV0Builder;
import org.motechproject.whp.v0.domain.PatientV0;
import org.motechproject.whp.v0.domain.TherapyV0;
import org.motechproject.whp.v0.domain.TreatmentCategoryV0;
import org.motechproject.whp.v0.domain.TreatmentV0;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PatientV1MapperTest {

    @Test
    public void shouldMapBasicPatientInfo() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(patientV0.getPatientId(), patient.getPatientId());
        assertEquals(patientV0.getFirstName(), patient.getFirstName());
        assertEquals(patientV0.getLastName(), patient.getLastName());
        assertEquals(patientV0.getGender().toString(), patient.getGender().toString());
        assertEquals(patientV0.getPhoneNumber(), patient.getPhoneNumber());
        assertEquals(patientV0.getPhi(), patient.getPhi());
        assertEquals(patientV0.getStatus().name(), patient.getStatus().name());
        assertEquals(patientV0.getLastModifiedDate(), patient.getLastModifiedDate());
        assertEquals(patientV0.isOnActiveTreatment(), patient.isOnActiveTreatment());
        assertEquals(patientV0.isMigrated(), patient.isMigrated());
        assertEquals("V2", patient.getVersion());
    }

    @Test
    public void shouldMapCurrentTherapy() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        Patient patient = new PatientV1Mapper(patientV0).map();
        assertTherapy(patientV0.getCurrentTreatment().getTherapy(), patient.getCurrentTherapy());
    }

    @Test
    public void shouldMapTherapyHistory_WhenCurrentTherapyHasOneTreatment() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TherapyV0 therapyV02 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc2").build();
        TherapyV0 therapyV03 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc3").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV02).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(therapyV03).build();
        patientV0.setTreatments(Arrays.asList(treatmentV01, treatmentV02));

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(2, patient.getTherapyHistory().size());
        assertTherapy(therapyV01, patient.getTherapyHistory().get(0));
        assertTherapy(therapyV02, patient.getTherapyHistory().get(1));
    }

    @Test
    public void shouldMapTherapyHistory_WhenCurrentTherapyHasMultipleTreatments() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TherapyV0 therapyV02 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc2").build();
        TherapyV0 currentTherapy = new TherapyV0Builder().withDefaults().withTherapyDocId("doc3").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV02).build();
        TreatmentV0 treatmentV03 = new TreatmentV0Builder().withDefaults().withTherapy(currentTherapy).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(currentTherapy).build();
        patientV0.setTreatments(Arrays.asList(treatmentV01, treatmentV02, treatmentV03));

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(2, patient.getTherapyHistory().size());
        assertTherapy(therapyV01, patient.getTherapyHistory().get(0));
        assertTherapy(therapyV02, patient.getTherapyHistory().get(1));
    }

    private void assertTherapy(TherapyV0 therapyV0, Therapy therapy) {
        assertEquals(therapyV0.getId(), therapy.getUid());
        assertEquals(therapyV0.getPatientAge(), therapy.getPatientAge());
        assertEquals(therapyV0.getCreationDate(), therapy.getCreationDate());
        assertEquals(therapyV0.getStartDate(), therapy.getStartDate());
        assertEquals(therapyV0.getCloseDate(), therapy.getCloseDate());
        assertEquals(therapyV0.getStatus().name(), therapy.getStatus().name());
        assertEquals(therapyV0.getDiseaseClass().name(), therapy.getDiseaseClass().name());
        assertTreatmentCategory(therapy.getTreatmentCategory(), therapyV0.getTreatmentCategory());
    }

    private void assertTreatmentCategory(TreatmentCategory treatmentCategory, TreatmentCategoryV0 treatmentCategoryV0) {
        assertEquals(treatmentCategoryV0.getName(), treatmentCategory.getName());
        assertEquals(treatmentCategoryV0.getCode(), treatmentCategory.getCode());
        assertEquals(treatmentCategoryV0.getDosesPerWeek(), treatmentCategory.getDosesPerWeek());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInIP(), treatmentCategory.getNumberOfDosesInIP());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInEIP(), treatmentCategory.getNumberOfDosesInEIP());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInCP(), treatmentCategory.getNumberOfDosesInCP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfIP(), treatmentCategory.getNumberOfWeeksOfIP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfEIP(), treatmentCategory.getNumberOfWeeksOfEIP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfCP(), treatmentCategory.getNumberOfWeeksOfCP());
        assertEquals(treatmentCategoryV0.getPillDays(), treatmentCategory.getPillDays());
    }

}
