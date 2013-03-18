package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.TreatmentDetails;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AssertTreatmentDetails {

    public static void assertTreatmentDetails(PatientRequest patientRequest, TreatmentDetails treatmentDetails){
        assertEquals(patientRequest.getDistrict_with_code(), treatmentDetails.getDistrictWithCode());
        assertEquals(patientRequest.getTb_unit_with_code(), treatmentDetails.getTbUnitWithCode());
        assertEquals(patientRequest.getEp_site(), treatmentDetails.getEpSite());
        assertEquals(patientRequest.getOther_investigations(), treatmentDetails.getOtherInvestigations());
        assertEquals(patientRequest.getPrevious_treatment_history(), treatmentDetails.getPreviousTreatmentHistory());
        assertEquals(patientRequest.getHiv_status(), treatmentDetails.getHivStatus());

        if(patientRequest.getHiv_test_date() != null){
            assertEquals(WHPDate.date(patientRequest.getHiv_test_date()).date(), treatmentDetails.getHivTestDate());
        } else {
            assertNull(treatmentDetails.getHivTestDate());
        }

        assertEquals(patientRequest.getMembers_below_six_years(), treatmentDetails.getMembersBelowSixYears());
        assertEquals(patientRequest.getPhc_referred() , treatmentDetails.getPhcReferred());
        assertEquals(patientRequest.getProvider_name(), treatmentDetails.getProviderName());
        assertEquals(patientRequest.getDot_centre(), treatmentDetails.getDotCentre());
        assertEquals(patientRequest.getProvider_type(), treatmentDetails.getProviderType());
        assertEquals(patientRequest.getCmf_doctor(), treatmentDetails.getCmfDoctor());
        assertEquals(patientRequest.getContact_person_name(), treatmentDetails.getContactPersonName());
        assertEquals(patientRequest.getContact_person_phone_number() , treatmentDetails.getContactPersonPhoneNumber());
        assertEquals(patientRequest.getXpert_test_result() , treatmentDetails.getXpertTestResult());
        assertEquals(patientRequest.getXpert_device_number() , treatmentDetails.getXpertDeviceNumber());

        if(patientRequest.getXpert_test_date() != null)
            assertEquals(WHPDate.date(patientRequest.getXpert_test_date()).date() , treatmentDetails.getXpertTestDate());
        else
            assertNull(treatmentDetails.getXpertTestDate());
        
        assertEquals(patientRequest.getRif_resistance_result(), treatmentDetails.getRifResistanceResult());
    }

}
