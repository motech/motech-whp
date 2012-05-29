package org.motechproject.whp.importer.csv;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.mapping.StringToDateTime;
import org.motechproject.whp.mapping.StringToEnumeration;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "patientRecordValidator", bean = ImportPatientRequest.class)
@Component
public class PatientRecordValidator {

    private AllPatients allPatients;
    private AllProviders allProviders;

    @Autowired
    public PatientRecordValidator(AllPatients allPatients, AllProviders allProviders) {
        this.allPatients = allPatients;
        this.allProviders = allProviders;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        return true;
    }

    @Post
    public void post(List<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            ImportPatientRequest request = (ImportPatientRequest) objects.get(i);
            List<String> errors = validatePatient(request);
            if (errors.size() > 0) {
                String errorMessage = String.format("Row %d: Patient with case_id \"%s\" has not been imported properly. These are the errors:\n", i + 1, request.getCase_id());
                for (String error : errors)
                    errorMessage += error + "\n";
                ImporterLogger.error(errorMessage);
            } else {
                ImporterLogger.info(String.format("Row %d: Patient with case_id \"%s\" has been imported properly", i + 1, request.getCase_id()));
            }
        }
    }

    private List<String> validatePatient(ImportPatientRequest request) {
        ArrayList<String> errors = new ArrayList<String>();
        Patient patient = allPatients.findByPatientId(request.getCase_id());
        if (patient == null) {
            errors.add("No patient found with case_id " + request.getCase_id());
        } else {
            validatePatientFields(patient, request, errors);
            validateProvidedTreatmentFields(patient, request, errors);
        }
        return errors;
    }

    private void validateProvidedTreatmentFields(Patient patient, ImportPatientRequest request, ArrayList<String> errors) {
        if (patient.getProvidedTreatments().size() != 0) {
            errors.add("Should have no history of provided treatments, but has count as " + patient.getProvidedTreatments().size());
        }

        ProvidedTreatment treatment = patient.getCurrentProvidedTreatment();
        checkIfDatesAreEqual(request.getDate_modified(), treatment.getStartDate(), "ProvidedTreatment's start date", errors, LocalDate.class);
        Address patientAddress = treatment.getPatientAddress();
        checkIfEqual(request.getAddress_district(), patientAddress.getAddress_district(), "District", errors);
        checkIfEqual(request.getAddress_block(), patientAddress.getAddress_block(), "Address Block", errors);
        checkIfEqual(request.getAddress_landmark(), patientAddress.getAddress_landmark(), "Landmark", errors);
        checkIfEqual(request.getAddress_location(), patientAddress.getAddress_location(), "Location", errors);
        checkIfEqual(request.getAddress_state(), patientAddress.getAddress_state(), "State", errors);
        checkIfEqual(request.getAddress_village(), patientAddress.getAddress_village(), "Village", errors);
        checkIfEqual(request.getProvider_id(), treatment.getProviderId(), "Treatment's providerId", errors);
        checkIfEqual(request.getTb_id(), treatment.getTbId(), "TbID", errors);
        checkIfEqual(request.getTb_registration_number(), treatment.getTbRegistrationNumber(), "TbRegistrationNumber", errors);

        if (request.getPatient_type().isEmpty()) {
            checkIfEqual(PatientType.New, treatment.getPatientType(), "Patient Type", errors);
        } else {
            checkIfEnumsAreEqual(request.getPatient_type(), treatment.getPatientType(), "Patient Type", errors, PatientType.class);
        }

    }

    private void validatePatientFields(Patient patient, ImportPatientRequest request, ArrayList<String> errors) {
        if (!patient.isMigrated()) errors.add("Patient not set as migrated");

        checkIfEqual(request.getProvider_id(), patient.providerId(), "ProviderId", errors);

        if (allProviders.findByProviderId(patient.providerId()) == null) {
            errors.add("Provider with Id \"" + patient.providerId() + "\" does not exist");
        }

        checkIfEqual(Integer.parseInt(request.getAge()), patient.getAge(), "Age", errors);
        checkIfEqual(request.getFirst_name(), patient.getFirstName(), "FirstName", errors);
        checkIfEqual(request.getLast_name(), patient.getLastName(), "LastName", errors);
        checkIfEqual(request.getMobile_number(), patient.getPhoneNumber(), "PhoneNumber", errors);
        if (StringUtils.isBlank(request.getPhi()))
            checkIfEqual("WHP", patient.getPhi(), "PHI", errors);
        else
            checkIfEqual(request.getPhi(), patient.getPhi(), "PHI", errors);
        checkIfDatesAreEqual(request.getDate_modified(), patient.getLastModifiedDate(), "LastModifiedDate", errors, DateTime.class);
        checkIfEnumsAreEqual(request.getGender(), patient.getGender(), "Gender", errors, Gender.class);
    }

    private void checkIfEnumsAreEqual(String requestEnum, Object patientEnum, String fieldName, ArrayList<String> errors, Class enumClass) {

        Object expectedEnum = new StringToEnumeration().convert(requestEnum, enumClass);
        if (expectedEnum == null)
            printInvalidDataError(fieldName, errors, requestEnum);
        checkIfEqual(expectedEnum, patientEnum, fieldName, errors);
    }

    private void checkIfDatesAreEqual(String requestDate, Object patientDate, String fieldName, ArrayList<String> errors, Class dateClass) {
        try {
            if (dateClass == DateTime.class) {
                String expectedDate = ((DateTime) new StringToDateTime().convert(requestDate, dateClass)).toString(WHPConstants.DATE_TIME_FORMAT);
                checkIfEqual(expectedDate, ((DateTime) patientDate).toString(WHPConstants.DATE_TIME_FORMAT), fieldName, errors);
            } else {
                String expectedDate = ((LocalDate) new StringToDateTime().convert(requestDate, dateClass)).toString(WHPConstants.DATE_FORMAT);
                checkIfEqual(expectedDate, ((LocalDate) patientDate).toString(WHPConstants.DATE_FORMAT), fieldName, errors);
            }
        } catch (Exception e) {
            printInvalidDataError(fieldName, errors, requestDate);
        }
    }

    private void printInvalidDataError(String fieldName, ArrayList<String> errors, String data) {
        errors.add("Invalid data provided for \"" + fieldName + "\": " + data);
    }

    private void checkIfEqual(Object expected, Object actual, String fieldName, List<String> errors) {
        if (expected != null && !expected.equals(actual))
            errors.add(fieldName + " should be set as \"" + expected + "\" but is set as \"" + actual + "\"");
    }

}

