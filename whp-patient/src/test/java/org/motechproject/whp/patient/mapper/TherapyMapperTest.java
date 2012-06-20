package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapTreatment;

public class TherapyMapperTest {

    @Test
    public void createNewTreatmentFromTreatmentUpdateRequest_SetsTreatmentCategory_DiseaseClass_WeightStatistics_LabResults_AndProviderId() {
        Patient patient = mapPatient(new PatientRequestBuilder().withDefaults().build());

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withTbId("newTbId")
                .build();

        Therapy newTherapy = TherapyMapper.createNewTreatment(patient, openNewTreatmentUpdateRequest);

        assertEquals(openNewTreatmentUpdateRequest.getDisease_class(), newTherapy.getDiseaseClass());
        assertEquals(patient.latestTherapy().getPatientAge(), newTherapy.getPatientAge());
        assertEquals(openNewTreatmentUpdateRequest.getTreatment_category(), newTherapy.getTreatmentCategory());
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Therapy therapy = TherapyMapper.map(patientRequest);
        Treatment treatment = mapTreatment(patientRequest, therapy);
        patient.addTreatment(treatment, patientRequest.getDate_modified());
        return patient;
    }

}
