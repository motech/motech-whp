package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class TreatmentMapper {

    public Treatment map(PatientRequest patientRequest) {

        Category category = Category.get(patientRequest.getTreatment_category());
        DiseaseClass diseaseClass = DiseaseClass.valueOf(patientRequest.getDisease_class());
        int patientAge = Integer.parseInt(patientRequest.getPatient_age());
        Treatment treatment = new Treatment(category, DateUtil.today(), diseaseClass, patientAge);

        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);

        return treatment;
    }

    private void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        String registrationNumber = patientRequest.getRegistration_number();
        LocalDate registrationDate = LocalDate.parse(patientRequest.getRegistration_date());
        treatment.setRegistrationDetails(registrationNumber, registrationDate);
    }


    private void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {
        LocalDate smearTestDate1 = LocalDate.parse(patientRequest.getSmear_test_date_1());
        LocalDate smearTestDate2 = LocalDate.parse(patientRequest.getSmear_test_date_2());
        treatment.addSmearTestResult(new SmearTestResult(
                patientRequest.getSmear_sample_instance_1(), smearTestDate1, patientRequest.getSmear_result_1(),
                patientRequest.getSmear_sample_instance_2(), smearTestDate2, patientRequest.getSmear_result_2()));
    }

    private void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightInstance weightInstance = WeightInstance.valueOf(patientRequest.getPatient_weight_instance());
        float weight = Float.parseFloat(patientRequest.getPatient_weight());
        treatment.addWeightStatistics(new WeightStatistics(weightInstance, weight, DateUtil.today()));
    }

}
