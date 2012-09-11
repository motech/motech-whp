package org.motechproject.whp.mapper;

import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientSummaryMapper {

    public static final String NAME_SEPARATOR = " ";

    public PatientSummaryMapper() {
    }


    public List<PatientSummary> map(List<Patient> patientList) {
        List<PatientSummary> patientSummaryList = new ArrayList<>();
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
        patientSummary.setDiseaseClass(extractDiseaseClass(patient));
        patientSummary.setIpTreatmentProgress(patient.getIPProgress());
        patientSummary.setName(buildPatientName(patient));
        patientSummary.setGender(patient.getGender());
        patientSummary.setPatientId(patient.getPatientId());
        patientSummary.setPatientType(extractPatientType(patient));
        patientSummary.setTbId(extractTbId(patient));
        patientSummary.setTbRegistrationDate(extractFormattedTbRegistrationDate(patient));
        patientSummary.setTreatmentCategory(extractTreatmentCategory(patient));
        patientSummary.setTreatmentStartDate(extractFormattedTreatmentStartDate(patient));
        patientSummary.setTreatmentClosingDate(extractFormattedTreatmentClosingDate(patient));
        patientSummary.setTreatmentOutcome(extractTreatmentOutcome(patient));
        patientSummary.setVillage(extractVillage(patient));
        patientSummary.setProviderId(extractProviderId(patient));
        patientSummary.setProviderDistrict(extractProviderDistrict(patient));
        return patientSummary;
    }

    private String extractProviderDistrict(Patient patient) {
        if(patient != null && patient.getCurrentTreatment() != null) {
            return patient.getCurrentTreatment().getProviderDistrict();
        }
        return null;
    }

    private String extractProviderId(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null) {
            return patient.getCurrentTreatment().getProviderId();
        }
        return null;
    }

    private String extractVillage(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getPatientAddress() != null) {
            return patient.getCurrentTreatment().getPatientAddress().getAddress_village();
        }
        return null;
    }

    private String extractTreatmentOutcome(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getTreatmentOutcome() != null) {
            return patient.getCurrentTreatment().getTreatmentOutcome().getOutcome();
        }
        return null;
    }

    private String extractFormattedTreatmentClosingDate(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getEndDate() != null) {
            return new WHPDate(patient.getCurrentTreatment().getEndDate()).value();
        }
        return null;
    }

    private String extractFormattedTreatmentStartDate(Patient patient) {
        if (patient != null && patient.getCurrentTherapy() != null && patient.getCurrentTherapy().getStartDate() != null) {
            return new WHPDate(patient.getCurrentTherapy().getStartDate()).value();
        }
        return null;
    }

    private String extractTreatmentCategory(Patient patient) {
        if (patient != null && patient.getCurrentTherapy() != null && patient.getCurrentTherapy().getTreatmentCategory() != null) {
            return patient.getCurrentTherapy().getTreatmentCategory().getName();
        }
        return null;
    }

    private String extractFormattedTbRegistrationDate(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getStartDate() != null) {
            return new WHPDate(patient.getCurrentTreatment().getStartDate()).value();
        }
        return null;
    }

    private String extractTbId(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null) {
            return patient.getCurrentTreatment().getTbId();
        }
        return null;
    }

    private PatientType extractPatientType(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null) {
            return patient.getCurrentTreatment().getPatientType();
        }
        return null;
    }

    private String extractDiseaseClass(Patient patient) {
        if (patient != null && patient.getCurrentTherapy() != null && patient.getCurrentTherapy().getDiseaseClass() != null) {
            return patient.getCurrentTherapy().getDiseaseClass().value();
        }
        return null;
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
