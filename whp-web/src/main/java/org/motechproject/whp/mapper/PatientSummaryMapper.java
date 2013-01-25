package org.motechproject.whp.mapper;

import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
        patientSummary.setCpTreatmentProgress(getCPProgress(patient));
        patientSummary.setCumulativeMissedDoses(patient.getCumulativeDosesNotTaken());
        patientSummary.setDiseaseClass(extractDiseaseClass(patient));
        patientSummary.setIpTreatmentProgress(getIPProgress(patient));
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
        patientSummary.setPreTreatmentSputumResult(extractPreTreatmentSputumResult(patient));
        patientSummary.setPreTreatmentWeight(extractPreTreatmentWeightStatistics(patient));
        return patientSummary;
    }

    private String extractPreTreatmentWeightStatistics(Patient patient) {
        if(patient.getPreTreatmentWeightRecord() ==null)
            return null;
        return patient.getPreTreatmentWeightRecord().getWeight().toString();
    }

    private String extractPreTreatmentSputumResult(Patient patient) {
        if(patient.getPreTreatmentSputumResult() == null)
            return null;

        return patient.getPreTreatmentSputumResult().name();
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

    private Date extractFormattedTreatmentClosingDate(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getEndDate() != null) {
            return patient.getCurrentTreatment().getEndDate().toDate();
        }
        return null;
    }

    private Date extractFormattedTreatmentStartDate(Patient patient) {
        if (patient != null && patient.getCurrentTherapy() != null && patient.getCurrentTherapy().getStartDate() != null) {
            return patient.getCurrentTherapy().getStartDate().toDate();
        }
        return null;
    }

    private String extractTreatmentCategory(Patient patient) {
        if (patient != null && patient.getCurrentTherapy() != null && patient.getCurrentTherapy().getTreatmentCategory() != null) {
            return patient.getCurrentTherapy().getTreatmentCategory().getName();
        }
        return null;
    }

    private Date extractFormattedTbRegistrationDate(Patient patient) {
        if (patient != null && patient.getCurrentTreatment() != null && patient.getCurrentTreatment().getStartDate() != null) {
            return patient.getCurrentTreatment().getStartDate().toDate();
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

    String getIPProgress(Patient patient) {
        int totalDoseTakenCount = patient.getCurrentTherapy().getNumberOfDosesTakenInIntensivePhases();
        int totalDoseCount = patient.getCurrentTherapy().getTotalDoesInIntensivePhases();

        return doseCompletionMessage(totalDoseCount, totalDoseTakenCount);
    }


    String getCPProgress(Patient patient) {
        int totalDoseCount = patient.getCurrentTherapy().getTotalDoesIn(Phase.CP);
        int totalDoseTakenCount = patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.CP);
        return doseCompletionMessage(totalDoseCount, totalDoseTakenCount);
    }

    private String doseCompletionMessage(int totalDoseCount, int totalDoseTakenCount) {
        if (totalDoseCount == 0) {
            return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, 0.0f);
        } else {
            return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, (totalDoseTakenCount / (float) totalDoseCount) * 100);
        }
    }

}

