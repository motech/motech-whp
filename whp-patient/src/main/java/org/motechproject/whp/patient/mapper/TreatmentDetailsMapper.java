package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.TreatmentDetails;
import org.springframework.stereotype.Component;

@Component
public class TreatmentDetailsMapper {

    public void map(PatientRequest patientRequest, Treatment treatment) {
        TreatmentDetails treatmentDetails = treatment.getTreatmentDetails();
        treatmentDetails.setEpSite(patientRequest.getEp_site());
        treatmentDetails.setDistrictWithCode(patientRequest.getDistrict_with_code());
        treatmentDetails.setTbIdWithCode(patientRequest.getTb_id_unit_with_code());
        treatmentDetails.setOtherInvestigations(patientRequest.getOther_investigations());
        treatmentDetails.setPreviousTreatmentHistory(patientRequest.getPrevious_treatment_history());
        treatmentDetails.setHivStatus(patientRequest.getHiv_status());

        if(patientRequest.getHiv_test_date() != null){
            treatmentDetails.setHivTestDate(patientRequest.getHiv_test_date().toLocalDate());
        }

        treatmentDetails.setMembersBelowSixYears(patientRequest.getMembers_below_six_years());
        treatmentDetails.setPhcReferred(patientRequest.getPhc_referred());
        treatmentDetails.setProviderName(patientRequest.getProvider_name());
        treatmentDetails.setDotCentre(patientRequest.getDot_centre());
        treatmentDetails.setProviderType(patientRequest.getProvider_type());
        treatmentDetails.setCmfDoctor(patientRequest.getCmf_doctor());
        treatmentDetails.setContactPersonName(patientRequest.getContact_person_name());
        treatmentDetails.setContactPersonPhoneNumber(patientRequest.getContact_person_phone_number());
        treatmentDetails.setXpertTestResult(patientRequest.getXpert_test_result());
        treatmentDetails.setXpertDeviceNumber(patientRequest.getXpert_device_number());

        if(patientRequest.getXpert_test_date() != null){
            treatmentDetails.setXpertTestDate(patientRequest.getXpert_test_date().toLocalDate());
        }
        treatmentDetails.setRifResistanceResult(patientRequest.getRif_resistance_result());
    }
}
