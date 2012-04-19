package org.motechproject.whp.mapper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class PatientMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");


    public Patient map(PatientRequest patientRequest, Treatment treatment) {
        Patient patient = mapBasicInfo(patientRequest);
        mapProvidedTreatment(patientRequest, patient, treatment);
        return patient;
    }

    private Patient mapBasicInfo(PatientRequest patientRequest) {
        Patient patient = new Patient(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                Gender.get(patientRequest.getGender()),
                PatientType.get(patientRequest.getPatient_type()),
                patientRequest.getPatient_mobile_num());
        patient.setLastModifiedDate(dateTimeFormatter.parseDateTime(patientRequest.getDate_modified()));
        return patient;
    }

    private void mapProvidedTreatment(PatientRequest patientRequest, Patient patient, Treatment treatment) {
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();

        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, tbId, DateUtil.today());
        providedTreatment.setTreatment(treatment);
        mapPatientAddress(patientRequest, providedTreatment);

        patient.addProvidedTreatment(providedTreatment);
    }

    private void mapPatientAddress(PatientRequest patientRequest, ProvidedTreatment providedTreatment) {
        providedTreatment.setPatientAddress(new Address(
                patientRequest.getPatient_address_house_num(),
                patientRequest.getPatient_address_landmark(),
                patientRequest.getPatient_address_block(),
                patientRequest.getPatient_address_village(),
                patientRequest.getPatient_address_district(),
                patientRequest.getPatient_address_state(),
                patientRequest.getPatient_postal_code()));
    }

}
