package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapProvidedTreatment;

public class TreatmentMapperTest {

    @Test
    public void createNewTreatmentFromTreatmentUpdateRequest_RetainsDiseaseClassAndPatientAge_SetsTreatmentCategory() {
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

        assertEquals(currentProvidedTreatment.getTreatment().getDiseaseClass(), newTreatment.getDiseaseClass());
        assertEquals(currentProvidedTreatment.getTreatment().getPatientAge(), newTreatment.getPatientAge());
        assertNotSame(currentProvidedTreatment.getTreatment().getTreatmentCategory(), newTreatment.getTreatmentCategory());
        assertEquals(openNewTreatmentUpdateRequest.getTreatment_category(), newTreatment.getTreatmentCategory());
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Treatment treatment = TreatmentMapper.map(patientRequest);
        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
        return patient;
    }

}
