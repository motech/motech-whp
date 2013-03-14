package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.TreatmentDetails;
import org.springframework.stereotype.Component;

@Component
public class TreatmentDetailsMapper {

    public TreatmentDetails map(PatientRequest patientRequest, Treatment treatment) {
        TreatmentDetails treatmentDetails = treatment.getTreatmentDetails();
        treatmentDetails.setEpSite(patientRequest.getEp_site());
        treatmentDetails.setDistrictWithCode(patientRequest.getDistrict_with_code());
        treatmentDetails.setTbUnitWithCode(patientRequest.getTb_unit_with_code());
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
        return treatmentDetails;
    }

    public TreatmentDetails mapWithNullCheck(PatientRequest patientRequest, Treatment treatment) {
        TreatmentDetails treatmentDetails = treatment.getTreatmentDetails();

        if (patientRequest.getEp_site() != null)
            treatmentDetails.setEpSite(patientRequest.getEp_site());
        if (patientRequest.getDistrict_with_code() != null)
            treatmentDetails.setDistrictWithCode(patientRequest.getDistrict_with_code());
        if (patientRequest.getTb_unit_with_code() != null)
            treatmentDetails.setTbUnitWithCode(patientRequest.getTb_unit_with_code());
        if (patientRequest.getOther_investigations() != null)
            treatmentDetails.setOtherInvestigations(patientRequest.getOther_investigations());
        if (patientRequest.getPrevious_treatment_history() != null)
            treatmentDetails.setPreviousTreatmentHistory(patientRequest.getPrevious_treatment_history());
        if (patientRequest.getHiv_status() != null)
            treatmentDetails.setHivStatus(patientRequest.getHiv_status());
        if (patientRequest.getHiv_test_date() != null)
            treatmentDetails.setHivTestDate(patientRequest.getHiv_test_date().toLocalDate());
        if (patientRequest.getMembers_below_six_years() != null)
            treatmentDetails.setMembersBelowSixYears(patientRequest.getMembers_below_six_years());
        if (patientRequest.getPhc_referred() != null)
            treatmentDetails.setPhcReferred(patientRequest.getPhc_referred());
        if (patientRequest.getProvider_name() != null)
            treatmentDetails.setProviderName(patientRequest.getProvider_name());
        if (patientRequest.getDot_centre() != null)
            treatmentDetails.setDotCentre(patientRequest.getDot_centre());
        if (patientRequest.getProvider_type() != null)
            treatmentDetails.setProviderType(patientRequest.getProvider_type());
        if (patientRequest.getCmf_doctor() != null)
            treatmentDetails.setCmfDoctor(patientRequest.getCmf_doctor());
        if (patientRequest.getContact_person_name() != null)
            treatmentDetails.setContactPersonName(patientRequest.getContact_person_name());
        if (patientRequest.getContact_person_phone_number() != null)
            treatmentDetails.setContactPersonPhoneNumber(patientRequest.getContact_person_phone_number());
        if (patientRequest.getXpert_test_result() != null)
            treatmentDetails.setXpertTestResult(patientRequest.getXpert_test_result());
        if (patientRequest.getXpert_device_number() != null)
            treatmentDetails.setXpertDeviceNumber(patientRequest.getXpert_device_number());
        if (patientRequest.getXpert_test_date() != null)
            treatmentDetails.setXpertTestDate(patientRequest.getXpert_test_date().toLocalDate());
        if (patientRequest.getRif_resistance_result() != null)
            treatmentDetails.setRifResistanceResult(patientRequest.getRif_resistance_result());

        return treatmentDetails;
    }
}
