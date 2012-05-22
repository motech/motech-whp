package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapProvidedTreatment;

public class TreatmentMapperTest {

    @Test
    public void createNewTreatmentFromTreatmentUpdateRequest_SetsTreatmentCategory_DiseaseClass_WeightStatistics_LabResults_AndProviderId() {
        Patient patient = mapPatient(new PatientRequestBuilder().withDefaults().build());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withTbId("newTbId")
                .build();

        Treatment newTreatment = TreatmentMapper.createNewTreatment(patient, openNewTreatmentUpdateRequest);

        assertEquals(openNewTreatmentUpdateRequest.getDisease_class(), newTreatment.getDiseaseClass());
        assertEquals(currentProvidedTreatment.getTreatment().getPatientAge(), newTreatment.getPatientAge());
        assertEquals(openNewTreatmentUpdateRequest.getTreatment_category(), newTreatment.getTreatmentCategory());

        SmearTestResults smearTestResults = openNewTreatmentUpdateRequest.getSmearTestResults();
        WeightStatistics weightStatistics = openNewTreatmentUpdateRequest.getWeightStatistics();

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
