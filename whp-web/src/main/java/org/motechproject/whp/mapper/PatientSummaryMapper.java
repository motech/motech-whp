package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientSummaryMapper {
    public PatientSummaryMapper() {
    }


    public List<PatientSummary> map(List<Patient> patientList) {
        List<PatientSummary> patientSummaryList = new ArrayList<PatientSummary>();
        for (Patient patient : patientList) {
            PatientSummary patientSummary = map(patient);
            patientSummaryList.add(patientSummary);
        }
        return patientSummaryList;
    }

    private PatientSummary map(Patient patient) {
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setAge(patient.getAge());
        patientSummary.setCpTreatmentProgress(patient.getCPProgress());
        patientSummary.setCumulativeMissedDoses(patient.getCumulativeDosesNotTaken());
        patientSummary.setDiseaseClass(patient.getCurrentTherapy().getDiseaseClass().value());
        patientSummary.setIpTreatmentProgress(patient.getIPProgress());
        patientSummary.setName(buildPatientName(patient));
        patientSummary.setGender(patient.getGender());
        patientSummary.setPatientId(patient.getPatientId());
        patientSummary.setPatientType(patient.getCurrentTreatment().getPatientType());
        patientSummary.setTbId(patient.getCurrentTreatment().getTbId());
        patientSummary.setTbRegistrationDate(new LocalDate(patient.getCurrentTherapy().getCreationDate()));
        patientSummary.setTreatmentCategory(patient.getCurrentTherapy().getTreatmentCategory().getName());
        patientSummary.setTreatmentStartDate(patient.getCurrentTreatment().getStartDate());
        patientSummary.setTreatmentClosingDate(patient.getCurrentTreatment().getEndDate());
        patientSummary.setTreatmentOutcome(patient.getCurrentTreatment().getTreatmentOutcome());
        patientSummary.setVillage(patient.getCurrentTreatment().getPatientAddress().getAddress_village());
        patientSummary.setProviderId(patient.getCurrentTreatment().getProviderId());
        patientSummary.setProviderDistrict("");
        return patientSummary;
    }

    private String buildPatientName(Patient patient) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtils.hasText(patient.getFirstName())) {
            nameBuilder.append(patient.getFirstName());
        }
        if (StringUtils.hasText(patient.getLastName())) {
            nameBuilder.append(" ");
            nameBuilder.append(patient.getLastName());
        }
        return nameBuilder.toString();
    }
}
