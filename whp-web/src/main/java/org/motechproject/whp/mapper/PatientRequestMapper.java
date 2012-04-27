package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class PatientRequestMapper {

    protected final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");

    public CreatePatientRequest map(PatientRequest patientRequest) {
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();

        mapBasicInfo(patientRequest, createPatientRequest);
        mapPatientAddress(patientRequest, createPatientRequest);
        mapTreatmentData(patientRequest, createPatientRequest);
        mapSmearTestResults(patientRequest, createPatientRequest);
        mapWeightStatistics(patientRequest, createPatientRequest);
        return createPatientRequest;
    }

    protected void mapPatientAddress(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        createPatientRequest.setPatientAddress(
                patientRequest.getAddress_location(),
                patientRequest.getAddress_landmark(),
                patientRequest.getAddress_block(),
                patientRequest.getAddress_village(),
                patientRequest.getAddress_district(),
                patientRequest.getAddress_state());
    }

    protected void mapBasicInfo(PatientRequest patientRequest, CreatePatientRequest createpatientrequest) {
        Gender gender = patientRequest.getGender() == null ? null : Gender.valueOf(patientRequest.getGender());
        PatientType patientType = patientRequest.getPatient_type() == null ? null : PatientType.valueOf(patientRequest.getPatient_type());
        DateTime lastModifiedDate = patientRequest.getDate_modified() == null ? null : dateTimeFormatter.parseDateTime(patientRequest.getDate_modified());
        createpatientrequest.setPatientInfo(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                gender,
                patientType,
                patientRequest.getMobile_number(),
                patientRequest.getPhi());
        createpatientrequest.setLastModifiedDate(lastModifiedDate);
    }

    protected void mapTreatmentData(PatientRequest patientRequest, CreatePatientRequest CreatePatientRequest) {
        TreatmentCategory treatment_category = patientRequest.getTreatment_category() == null ? null : TreatmentCategory.get(patientRequest.getTreatment_category());
        DiseaseClass disease_class = patientRequest.getDisease_class() == null ? null : DiseaseClass.valueOf(patientRequest.getDisease_class());
        DateTime treatmentStartDate = patientRequest.getDate_modified() == null ? null : dateTimeFormatter.parseDateTime(patientRequest.getDate_modified());
        CreatePatientRequest.setTreatmentData(
                treatment_category,
                patientRequest.getTb_id(),
                patientRequest.getProvider_id(),
                disease_class,
                Integer.parseInt(patientRequest.getAge()),
                patientRequest.getTb_registration_number(),
                treatmentStartDate);
    }

    protected void mapSmearTestResults(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {

        SmearTestSampleInstance smearSampleInstance = patientRequest.getSmear_sample_instance() == null ? null : SmearTestSampleInstance.valueOf(patientRequest.getSmear_sample_instance());
        LocalDate smearTestDate1 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_1());
        SmearTestResult smearTestResult1 = patientRequest.getSmear_test_result_1() == null ? null : SmearTestResult.valueOf(patientRequest.getSmear_test_result_1());

        LocalDate smearTestDate2 = localDateFormatter.parseLocalDate(patientRequest.getSmear_test_date_2());
        SmearTestResult smearTestResult2 = patientRequest.getSmear_test_result_2() == null ? null : SmearTestResult.valueOf(patientRequest.getSmear_test_result_2());

        createPatientRequest.setSmearTestResults(smearSampleInstance, smearTestDate1, smearTestResult1, smearTestDate2, smearTestResult2);
    }

    protected void mapWeightStatistics(PatientRequest patientRequest, CreatePatientRequest createPatientRequest) {
        WeightInstance weightInstance = patientRequest.getWeight_instance() == null ?  null : WeightInstance.valueOf(patientRequest.getWeight_instance());
        Double weight = patientRequest.getWeight() == null ? null : Double.parseDouble(patientRequest.getWeight());
        createPatientRequest.setWeightStatistics(weightInstance, weight, dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()).toLocalDate());
    }
}
