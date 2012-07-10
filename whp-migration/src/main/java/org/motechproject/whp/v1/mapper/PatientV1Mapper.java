package org.motechproject.whp.v1.mapper;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.v0.domain.PatientV0;
import org.motechproject.whp.v0.domain.TherapyV0;
import org.motechproject.whp.v0.domain.TreatmentCategoryV0;

import java.util.ArrayList;
import java.util.List;

public class PatientV1Mapper {

    private PatientV0 patientV0;
    private Patient patient;

    public PatientV1Mapper(PatientV0 patientV0) {
        this.patientV0 = patientV0;
    }

    public Patient map() {
        patient = new Patient();
        mapBasicInfo();
        mapCurrentTherapy();
        mapTherapyHistory();

        return patient;
    }

    private void mapTherapyHistory() {
        ArrayList<Therapy> therapies = new ArrayList<>();
        List<TherapyV0> therapyV0List = patientV0.getTherapyHistory();
        for (TherapyV0 therapyV0 : therapyV0List) {
            therapies.add(mapTherapy(therapyV0));
        }
        patient.setTherapyHistory(therapies);
    }

    private void mapCurrentTherapy() {
        TherapyV0 therapyV0 = patientV0.getCurrentTreatment().getTherapy();
        Therapy currentTherapy = mapTherapy(therapyV0);
        patient.setCurrentTherapy(currentTherapy);
    }

    private Therapy mapTherapy(TherapyV0 therapyV0) {
        Therapy therapy = new Therapy();

        therapy.setUid(therapyV0.getId());
        therapy.setPatientAge(therapyV0.getPatientAge());
        therapy.setCreationDate(therapyV0.getCreationDate());
        therapy.setStartDate(therapyV0.getStartDate());
        therapy.setCloseDate(therapyV0.getCloseDate());
        therapy.setStatus(TherapyStatus.valueOf(therapyV0.getStatus().name()));
        therapy.setTreatmentCategory(treatmentCategory(therapyV0.getTreatmentCategory()));
        therapy.setDiseaseClass(DiseaseClass.valueOf(therapyV0.getDiseaseClass().name()));
        return therapy;
    }

    private TreatmentCategory treatmentCategory(TreatmentCategoryV0 treatmentCategoryV0) {
        return new TreatmentCategory(treatmentCategoryV0.getName(), treatmentCategoryV0.getCode(), treatmentCategoryV0.getDosesPerWeek(),
                treatmentCategoryV0.getNumberOfWeeksOfIP(), treatmentCategoryV0.getNumberOfDosesInIP(),
                treatmentCategoryV0.getNumberOfWeeksOfEIP(), treatmentCategoryV0.getNumberOfDosesInEIP(),
                treatmentCategoryV0.getNumberOfWeeksOfCP(), treatmentCategoryV0.getNumberOfDosesInCP(),
                treatmentCategoryV0.getPillDays());
    }

    private void mapBasicInfo() {
        patient.setPatientId(patientV0.getPatientId());
        patient.setFirstName(patientV0.getFirstName());
        patient.setLastName(patientV0.getLastName());
        patient.setGender(Gender.valueOf(patientV0.getGender().name()));
        patient.setPhoneNumber(patientV0.getPhoneNumber());
        patient.setPhi(patientV0.getPhi());
        patient.setStatus(PatientStatus.valueOf(patientV0.getStatus().name()));
        patient.setLastModifiedDate(patientV0.getLastModifiedDate());
        patient.setOnActiveTreatment(patientV0.isOnActiveTreatment());
        patient.setMigrated(patientV0.isMigrated());
    }
}
