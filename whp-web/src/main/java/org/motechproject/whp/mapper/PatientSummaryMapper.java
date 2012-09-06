package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PatientSummaryMapper {

    public static final String EXPORT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String NAME_SEPARATOR = " ";

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
        patientSummary.setTbRegistrationDate(formatDate(new LocalDate(patient.getCurrentTherapy().getCreationDate())));
        patientSummary.setTreatmentCategory(patient.getCurrentTherapy().getTreatmentCategory().getName());
        patientSummary.setTreatmentStartDate(formatDate(patient.getCurrentTreatment().getStartDate()));
        patientSummary.setTreatmentClosingDate(patient.getCurrentTreatment().getEndDate());
        patientSummary.setTreatmentOutcome(patient.getCurrentTreatment().getTreatmentOutcome());
        patientSummary.setVillage(patient.getCurrentTreatment().getPatientAddress().getAddress_village());
        patientSummary.setProviderId(patient.getCurrentTreatment().getProviderId());
        patientSummary.setProviderDistrict("");
        return patientSummary;
    }

    private String formatDate(LocalDate date) {
        return new SimpleDateFormat(EXPORT_DATE_FORMAT).format(date.toDate());
    }

    private String buildPatientName(Patient patient) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtils.hasText(patient.getFirstName())) {
            nameBuilder.append(patient.getFirstName());
        }
        if (StringUtils.hasText(patient.getLastName())) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(NAME_SEPARATOR);
            }
            nameBuilder.append(patient.getLastName());
        }
        return nameBuilder.toString();
    }
}
