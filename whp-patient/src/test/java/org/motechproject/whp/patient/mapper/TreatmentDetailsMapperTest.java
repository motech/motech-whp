package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.TreatmentDetails;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.motechproject.whp.patient.mapper.AssertTreatmentDetails.assertTreatmentDetails;

public class TreatmentDetailsMapperTest {
    
    TreatmentDetailsMapper treatmentDetailsMapper = new TreatmentDetailsMapper();
    
    @Test
    public void shouldUpdateTreatmentDetails() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaultTreatmentDetails().build();
        
        Treatment treatment = new Treatment();
        treatmentDetailsMapper.map(patientRequest, treatment);

        TreatmentDetails treatmentDetails = treatment.getTreatmentDetails();

        assertTreatmentDetails(patientRequest, treatmentDetails);
    }

    @Test
    public void shouldUpdateTreatmentDetails_whenOptionalFieldsAreNull() {

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaultTreatmentDetails().build();
        patientRequest.setXpert_test_date(null);
        patientRequest.setXpert_device_number(null);
        patientRequest.setXpert_test_result(null);
        patientRequest.setRif_resistance_result(null);
        patientRequest.setPhc_referred(null);
        patientRequest.setHiv_test_date(null);
        patientRequest.setDistrict_with_code(null);
        patientRequest.setTb_unit_with_code(null);
        patientRequest.setEp_site(null);
        patientRequest.setOther_investigations(null);
        patientRequest.setPrevious_treatment_history(null);

        Treatment treatment = new Treatment();
        treatmentDetailsMapper.map(patientRequest, treatment);

        TreatmentDetails treatmentDetails = treatment.getTreatmentDetails();

        assertThat(patientRequest.getDistrict_with_code(),is(treatmentDetails.getDistrictWithCode()));
        assertThat(patientRequest.getTb_unit_with_code(),is(treatmentDetails.getTbUnitWithCode()));
        assertThat(patientRequest.getEp_site(),is(treatmentDetails.getEpSite()));
        assertThat(patientRequest.getOther_investigations(),is(treatmentDetails.getOtherInvestigations()));
        assertThat(patientRequest.getPrevious_treatment_history(),is(treatmentDetails.getPreviousTreatmentHistory()));
        assertThat(patientRequest.getHiv_status(),is(treatmentDetails.getHivStatus()));
        assertNull(treatmentDetails.getHivTestDate());
        assertThat(patientRequest.getMembers_below_six_years(), is(treatmentDetails.getMembersBelowSixYears()));
        assertThat(patientRequest.getPhc_referred() ,is(treatmentDetails.getPhcReferred()));
        assertThat(patientRequest.getProvider_name(),is(treatmentDetails.getProviderName()));
        assertThat(patientRequest.getDot_centre(), is(treatmentDetails.getDotCentre()));
        assertThat(patientRequest.getProvider_type(),is(treatmentDetails.getProviderType()));
        assertThat(patientRequest.getCmf_doctor(),is(treatmentDetails.getCmfDoctor()));
        assertThat(patientRequest.getContact_person_name(),is(treatmentDetails.getContactPersonName()));
        assertThat(patientRequest.getContact_person_phone_number() ,is(treatmentDetails.getContactPersonPhoneNumber()));
        assertThat(patientRequest.getXpert_test_result() ,is(treatmentDetails.getXpertTestResult()));
        assertThat(patientRequest.getXpert_device_number() ,is(treatmentDetails.getXpertDeviceNumber()));
        assertNull(treatmentDetails.getXpertTestDate());
        assertThat(patientRequest.getRif_resistance_result(),is(treatmentDetails.getRifResistanceResult()));
    }
}
