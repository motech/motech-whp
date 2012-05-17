package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.*;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapProvidedTreatment;

public class TreatmentMapperTest {

    @Test
    public void createNewTreatmentFromTreatmentUpdateRequest_SetsTreatmentCategory_DiseaseClass_WeightStatistics_LabResults_AndProviderId() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Patient patient = mapPatient(patientRequest);

        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();

        TreatmentUpdateRequest openNewTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForOpenNewTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .build();

        Treatment newTreatment = TreatmentMapper.createNewTreatment(patient, openNewTreatmentUpdateRequest);

        assertEquals(openNewTreatmentUpdateRequest.getDisease_class(), newTreatment.getDiseaseClass());
        assertEquals(currentProvidedTreatment.getTreatment().getPatientAge(), newTreatment.getPatientAge());
        assertEquals(openNewTreatmentUpdateRequest.getTreatment_category(), newTreatment.getTreatmentCategory());

        SmearTestResults smearTestResults = new SmearTestResults(openNewTreatmentUpdateRequest.getSmear_sample_instance(),
                openNewTreatmentUpdateRequest.getSmear_test_date_1(),
                openNewTreatmentUpdateRequest.getSmear_test_result_1(),
                openNewTreatmentUpdateRequest.getSmear_test_date_2(),
                openNewTreatmentUpdateRequest.getSmear_test_result_2());

        WeightStatistics weightStatistics = new WeightStatistics(openNewTreatmentUpdateRequest.getWeight_instance(),
                openNewTreatmentUpdateRequest.getWeight(),
                openNewTreatmentUpdateRequest.getDate_modified().toLocalDate());


        assertEquals(smearTestResults, newTreatment.getSmearTestInstances().latestResult());
        assertEquals(weightStatistics, newTreatment.getWeightInstances().latestResult());
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Treatment treatment = TreatmentMapper.map(patientRequest);
        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
        return patient;
    }

}
