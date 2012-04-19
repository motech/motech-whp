package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class TreatmentMapper {

    private final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");

    public Treatment map(PatientRequest patientRequest) {

        Category category = Category.get(patientRequest.getTreatment_category());
        DiseaseClass diseaseClass = DiseaseClass.valueOf(patientRequest.getDisease_class());
        int patientAge = Integer.parseInt(patientRequest.getAge());
        Treatment treatment = new Treatment(category, DateUtil.today(), diseaseClass, patientAge);

        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);

        return treatment;
    }

    private void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        String registrationNumber = patientRequest.getTb_registration_number();
        LocalDate registrationDate = localDateFormatter.parseLocalDate(patientRequest.getRegistration_date());
        treatment.setRegistrationDetails(registrationNumber, registrationDate);
    }


    private void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {

        LocalDate smearTestDate1 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_1());
        SmearTestSampleInstance smearSampleInstance1 = SmearTestSampleInstance.valueOf(patientRequest.getSmear_sample_instance_1());

        LocalDate smearTestDate2 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_2());
        SmearTestSampleInstance smearSampleInstance2 = SmearTestSampleInstance.valueOf(patientRequest.getSmear_sample_instance_2());

        treatment.addSmearTestResult(new SmearTestResult(
                smearSampleInstance1, smearTestDate1, patientRequest.getSmear_test_result_1(),
                smearSampleInstance2, smearTestDate2, patientRequest.getSmear_test_result_2()));

    }

    private void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightInstance weightInstance = WeightInstance.valueOf(patientRequest.getWeight_instance());
        float weight = Float.parseFloat(patientRequest.getWeight());
        treatment.addWeightStatistics(new WeightStatistics(weightInstance, weight, DateUtil.today()));
    }

}
