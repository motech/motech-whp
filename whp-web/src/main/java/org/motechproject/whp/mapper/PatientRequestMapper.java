package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientRequestMapper {

    AllTreatmentCategories allTreatmentCategories;

    protected final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");

    @Autowired
    public PatientRequestMapper(AllTreatmentCategories allTreatmentCategories) {
        this.allTreatmentCategories = allTreatmentCategories;
    }
    
    public PatientRequest map(PatientWebRequest patientWebRequest) {
        PatientRequest patientRequest = new PatientRequest();

        mapBasicInfo(patientWebRequest, patientRequest);
        mapPatientAddress(patientWebRequest, patientRequest);
        mapTreatmentData(patientWebRequest, patientRequest);
        mapSmearTestResults(patientWebRequest, patientRequest);
        mapWeightStatistics(patientWebRequest, patientRequest);
        return patientRequest;
    }

    protected void mapPatientAddress(PatientWebRequest patientWebRequest, PatientRequest patientRequest) {
        patientRequest.setPatientAddress(
                patientWebRequest.getAddress_location(),
                patientWebRequest.getAddress_landmark(),
                patientWebRequest.getAddress_block(),
                patientWebRequest.getAddress_village(),
                patientWebRequest.getAddress_district(),
                patientWebRequest.getAddress_state());
    }

    protected void mapBasicInfo(PatientWebRequest patientWebRequest, PatientRequest patientRequest) {
        Gender gender = patientWebRequest.getGender() == null ? null : Gender.valueOf(patientWebRequest.getGender());
        PatientType patientType = patientWebRequest.getPatient_type() == null ? null : PatientType.valueOf(patientWebRequest.getPatient_type());
        DateTime lastModifiedDate = patientWebRequest.getDate_modified() == null ? null : dateTimeFormatter.parseDateTime(patientWebRequest.getDate_modified());
        patientRequest.setPatientInfo(
                patientWebRequest.getCase_id(),
                patientWebRequest.getFirst_name(),
                patientWebRequest.getLast_name(),
                gender,
                patientType,
                patientWebRequest.getMobile_number(),
                patientWebRequest.getPhi());
        patientRequest.setLastModifiedDate(lastModifiedDate);
    }

    protected void mapTreatmentData(PatientWebRequest patientWebRequest, PatientRequest patientRequest) {
        TreatmentCategory treatment_category = patientWebRequest.getTreatment_category() == null ? null : allTreatmentCategories.findByCode((patientWebRequest.getTreatment_category()));
        DiseaseClass disease_class = patientWebRequest.getDisease_class() == null ? null : DiseaseClass.valueOf(patientWebRequest.getDisease_class());
        DateTime treatmentStartDate = patientWebRequest.getDate_modified() == null ? null : dateTimeFormatter.parseDateTime(patientWebRequest.getDate_modified());
        patientRequest.setTreatmentData(
                treatment_category,
                patientWebRequest.getTb_id(),
                patientWebRequest.getProvider_id(),
                disease_class,
                Integer.parseInt(patientWebRequest.getAge()),
                patientWebRequest.getTb_registration_number(),
                treatmentStartDate);
    }

    protected void mapSmearTestResults(PatientWebRequest patientWebRequest, PatientRequest patientRequest) {

        SmearTestSampleInstance smearSampleInstance = patientWebRequest.getSmear_sample_instance() == null ? null : SmearTestSampleInstance.valueOf(patientWebRequest.getSmear_sample_instance());
        LocalDate smearTestDate1 = patientWebRequest.getSmear_test_date_1() == null ? null : localDateFormatter.parseLocalDate(patientWebRequest.getSmear_test_date_1());
        SmearTestResult smearTestResult1 = patientWebRequest.getSmear_test_result_1() == null ? null : SmearTestResult.valueOf(patientWebRequest.getSmear_test_result_1());

        LocalDate smearTestDate2 = patientWebRequest.getSmear_test_date_2() == null ? null : localDateFormatter.parseLocalDate(patientWebRequest.getSmear_test_date_2());
        SmearTestResult smearTestResult2 = patientWebRequest.getSmear_test_result_2() == null ? null : SmearTestResult.valueOf(patientWebRequest.getSmear_test_result_2());

        patientRequest.setSmearTestResults(smearSampleInstance, smearTestDate1, smearTestResult1, smearTestDate2, smearTestResult2);
    }

    protected void mapWeightStatistics(PatientWebRequest patientWebRequest, PatientRequest patientRequest) {
        WeightInstance weightInstance = patientWebRequest.getWeight_instance() == null ?  null : WeightInstance.valueOf(patientWebRequest.getWeight_instance());
        Double weight = patientWebRequest.getWeight() == null ? null : Double.parseDouble(patientWebRequest.getWeight());
        patientRequest.setWeightStatistics(weightInstance, weight, dateTimeFormatter.parseDateTime(patientWebRequest.getDate_modified()).toLocalDate());
    }
}
