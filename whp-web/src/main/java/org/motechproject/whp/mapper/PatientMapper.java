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
                PatientType.valueOf(patientRequest.getPatient_type()),
                patientRequest.getMobile_number());
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
                patientRequest.getAddress_house_number(),
                patientRequest.getAddress_landmark(),
                patientRequest.getAddress_block(),
                patientRequest.getAddress_village(),
                patientRequest.getAddress_district(),
                patientRequest.getAddress_state(),
                patientRequest.getAddress_postal_code()));
    }

}
