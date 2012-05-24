package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.DiseaseClass;

public class TreatmentMapper {

    public static Treatment map(PatientRequest patientRequest) {
        Treatment treatment = createFirstTreatment(patientRequest);
        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);
        return treatment;
    }

    public static Treatment createNewTreatment(Patient patient, PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();

        Treatment newTreatment = new Treatment(treatmentCategory, diseaseClass, patient.getAge(), patientRequest.getPatient_type());
        newTreatment.addSmearTestResult(patientRequest.getSmearTestResults());
        newTreatment.addWeightStatistics(patientRequest.getWeightStatistics());

        return newTreatment;
    }

    private static Treatment createFirstTreatment(PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();
        int patientAge = patientRequest.getAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge, patientRequest.getPatient_type());
    }

    private static void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        treatment.setCreationDate(patientRequest.getTreatmentStartDate());
    }

    static void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        if (!smearTestResults.isEmpty()) {
            treatment.addSmearTestResult(smearTestResults);
        }
    }

    static void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightStatistics weightStatistics = patientRequest.getWeightStatistics();
        if (!weightStatistics.isEmpty()) {
            treatment.addWeightStatistics(weightStatistics);
        }
    }

}
