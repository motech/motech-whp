package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
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

    public static Treatment createNewTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest) {
        TreatmentCategory treatmentCategory = treatmentUpdateRequest.getTreatment_category();
        DiseaseClass diseaseClass = treatmentUpdateRequest.getDisease_class();

        Treatment newTreatment = new Treatment(treatmentCategory, diseaseClass, patient.getAge());
        SmearTestResults smearTestResults = new SmearTestResults(treatmentUpdateRequest.getSmear_sample_instance(),
                treatmentUpdateRequest.getSmear_test_date_1(),
                treatmentUpdateRequest.getSmear_test_result_1(),
                treatmentUpdateRequest.getSmear_test_date_2(),
                treatmentUpdateRequest.getSmear_test_result_2());
        newTreatment.addSmearTestResult(smearTestResults);

        WeightStatistics weightStatistics = new WeightStatistics(treatmentUpdateRequest.getWeight_instance(),
                treatmentUpdateRequest.getWeight(),
                treatmentUpdateRequest.getDate_modified().toLocalDate());

        newTreatment.addWeightStatistics(weightStatistics);
        return newTreatment;
    }

    private static Treatment createFirstTreatment(PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();
        int patientAge = patientRequest.getAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge);
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
