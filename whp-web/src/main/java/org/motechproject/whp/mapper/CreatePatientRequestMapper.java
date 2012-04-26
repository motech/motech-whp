package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.request.PatientRequest;

public class CreatePatientRequestMapper {

    private final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");

    public CreatePatientRequest map(PatientRequest patientRequest) {
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();

        mapBasicInfo(patientRequest, createPatientRequest);
        mapPatientAddress(patientRequest, createPatientRequest);
        mapTreatmentData(patientRequest, createPatientRequest);
        mapSmearTestResults(patientRequest, createPatientRequest);
        mapWeightStatistics(patientRequest, createPatientRequest);
        return createPatientRequest;
    }

    private void mapTreatmentData(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        createPatientRequest.setTreatmentData(
                patientRequest.getTreatment_category(),
                patientRequest.getTb_id(),
                patientRequest.getProvider_id(),
                patientRequest.getDisease_class(),
                Integer.parseInt(patientRequest.getAge()),
                patientRequest.getTb_registration_number(),
                dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()));
    }

    private void mapBasicInfo(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        createPatientRequest.setPatientInfo(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                patientRequest.getGender(),
                patientRequest.getPatient_type(),
                patientRequest.getMobile_number(),
                patientRequest.getPhi());
    }

    private void mapPatientAddress(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        createPatientRequest.setPatientAddress(
                patientRequest.getAddress_location(),
                patientRequest.getAddress_landmark(),
                patientRequest.getAddress_block(),
                patientRequest.getAddress_village(),
                patientRequest.getAddress_district(),
                patientRequest.getAddress_state());
    }

    private void mapSmearTestResults(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {

        String smearSampleInstance = patientRequest.getSmear_sample_instance();
        LocalDate smearTestDate1 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_1());
        String smearTestResult1 = patientRequest.getSmear_test_result_1();

        LocalDate smearTestDate2 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_2());
        String smearTestResult2 = patientRequest.getSmear_test_result_2();

        createPatientRequest.setSmearTestResults(smearSampleInstance, smearTestDate1, smearTestResult1, smearTestDate2, smearTestResult2);
    }

    private void mapWeightStatistics(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        String weightInstance = patientRequest.getWeight_instance();
        float weight = Float.parseFloat(patientRequest.getWeight());
        createPatientRequest.setWeightStatistics(weightInstance, weight, dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()).toLocalDate());
    }

}
