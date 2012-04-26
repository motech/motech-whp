package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class TreatmentMapper {

    private final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");

    public Treatment map(PatientRequest patientRequest) {

        Category category = Category.get(patientRequest.getTreatment_category());
        DiseaseClass diseaseClass = DiseaseClass.valueOf(patientRequest.getDisease_class());
        int patientAge = Integer.parseInt(patientRequest.getAge());
        Treatment treatment = new Treatment(category, diseaseClass, patientAge);

        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);

        return treatment;
    }

    private void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        treatment.setRegistrationDate(dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()));
    }

    private void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {

        LocalDate smearTestDate1 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_1());
        SmearTestSampleInstance smearSampleInstance1 = SmearTestSampleInstance.valueOf(patientRequest.getSmear_sample_instance());
        SmearTestResult smearTestResult1 = SmearTestResult.valueOf(patientRequest.getSmear_test_result_1());

        LocalDate smearTestDate2 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_2());
        SmearTestResult smearTestResult2 = SmearTestResult.valueOf(patientRequest.getSmear_test_result_2());

        treatment.addSmearTestResult(new SmearTestResults(
                smearSampleInstance1, smearTestDate1, smearTestResult1,
                smearTestDate2, smearTestResult2));
    }

    private void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightInstance weightInstance = WeightInstance.valueOf(patientRequest.getWeight_instance());
        float weight = Float.parseFloat(patientRequest.getWeight());
        treatment.addWeightStatistics(new WeightStatistics(weightInstance, weight, dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()).toLocalDate()));
    }

}
