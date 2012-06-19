package org.motechproject.whp.importer.csv;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.importer.csv.request.SmearTestResultRequests;
import org.motechproject.whp.importer.csv.request.WeightStatisticsRequests;
import org.motechproject.whp.mapping.StringToDateTime;
import org.motechproject.whp.mapping.StringToEnumeration;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.util.StringUtils.hasText;

@CSVImporter(entity = "patientRecordValidator", bean = ImportPatientRequest.class)
@Component
public class PatientRecordValidator {

    public static final StringToDateTime stringToDateTime = new StringToDateTime();
    private AllPatients allPatients;
    private AllProviders allProviders;
    private static final StringToEnumeration stringToEnumeration = new StringToEnumeration();

    @Autowired
    public PatientRecordValidator(AllPatients allPatients, AllProviders allProviders) {
        this.allPatients = allPatients;
        this.allProviders = allProviders;
    }

    @Validate
    public ValidationResponse validate(List<Object> objects) {
        return new ValidationResponse(true);
    }

    @Post
    public void post(List<Object> objects) throws Exception {
        try {
            int objectsInDbCount = 0;
            for (int i = 0; i < objects.size(); i++) {
                ImportPatientRequest request = (ImportPatientRequest) objects.get(i);
                ArrayList<String> errors = new ArrayList<String>();

                Patient patient = allPatients.findByPatientId(request.getCase_id());
                if (patient == null) {
                    errors.add("No patient found with case_id " + request.getCase_id());
                    continue;
                }

                objectsInDbCount++;
                validatePatient(patient, request, errors);

                if (errors.size() > 0) {
                    String errorMessage = String.format("Row %d: Patient with case_id \"%s\" has not been imported properly. These are the errors:\n", i + 1, request.getCase_id());
                    for (String error : errors)
                        errorMessage += error + "\n";
                    ImporterLogger.error(errorMessage);
                } else {
                    ImporterLogger.info(String.format("Row %d: Patient with case_id \"%s\" has been imported properly", i + 1, request.getCase_id()));
                }
            }
            ImporterLogger.info("Number of Objects found in the data file : " + objects.size());
            ImporterLogger.info("Number of Objects found in the db : " + objectsInDbCount);
        } catch (Exception exception) {
            ImporterLogger.error("Exception occured while testing...");
            ImporterLogger.error(exception);
        }
    }

    private void validatePatient(Patient patient, ImportPatientRequest request, List<String> errors) {
        validatePatientFields(patient, request, errors);
        validateTreatmentFields(patient, request, errors);
    }

    private void validatePatientFields(Patient patient, ImportPatientRequest request, List<String> errors) {
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

    private void validateTreatmentFields(Patient patient, ImportPatientRequest request, List<String> errors) {
        if (patient.getTreatments().size() != 0) {
            errors.add("Should have no history of treatments, but has count as " + patient.getTreatments().size());
        }

        Treatment treatment = patient.getCurrentTreatment();
        checkIfDatesAreEqual(request.getDate_modified(), treatment.getStartDate(), "TreatmentStartDate", errors, LocalDate.class);
        Address patientAddress = treatment.getPatientAddress();
        checkIfEqual(request.getAddress_district(), patientAddress.getAddress_district(), "District", errors);
        checkIfEqual(request.getAddress_block(), patientAddress.getAddress_block(), "Address Block", errors);
        checkIfEqual(request.getAddress_landmark(), patientAddress.getAddress_landmark(), "Landmark", errors);
        checkIfEqual(request.getAddress_location(), patientAddress.getAddress_location(), "Location", errors);
        checkIfEqual(request.getAddress_state(), patientAddress.getAddress_state(), "State", errors);
        checkIfEqual(request.getAddress_village(), patientAddress.getAddress_village(), "Village", errors);
        checkIfEqual(request.getProvider_id(), treatment.getProviderId(), "Therapy's providerId", errors);
        checkIfEqual(request.getTb_id(), treatment.getTbId(), "TbID", errors);
        checkIfEqual(request.getTb_registration_number(), treatment.getTbRegistrationNumber(), "TbRegistrationNumber", errors);

        if (StringUtils.isBlank(request.getPatient_type())) {
            checkIfEqual(PatientType.New, treatment.getPatientType(), "Patient Type", errors);
        } else {
            checkIfEnumsAreEqual(request.getPatient_type(), treatment.getPatientType(), "Patient Type", errors, PatientType.class);
        }

        validateTreatment(patient.getCurrentTreatment().getTherapy(), request, errors);
        validateSmearTestResults(treatment.getSmearTestResults(), errors, request.getSmearTestResultRequest());
        validateWeightStatistics(request, errors, patient.getCurrentTreatment().getWeightStatistics());

    }

    private void validateTreatment(Therapy therapy, ImportPatientRequest request, List<String> errors) {
        if (therapy != null) {
            TreatmentCategory treatmentCategory = therapy.getTreatmentCategory();
            if (treatmentCategory != null)
                checkIfEqual(request.getTreatment_category(), treatmentCategory.getCode(), "Treatement Category", errors);
            else
                addNotSetError("Therapy Category", request.getTreatment_category(), errors);
            checkIfDatesAreEqual(request.getDate_modified(), therapy.getCreationDate(), "TreatmentCreationDate", errors, DateTime.class);
            if (therapy.getStartDate() != null)
                errors.add("TreatmentStartDate should be set as null for migrated patients. But is set to \"" + therapy.getStartDate() + "\"");

        } else {
            addNotSetError("Therapy Category", request.getTreatment_category(), errors);
            addNotSetError("Disease Class", request.getDisease_class(), errors);
            addNotSetError("Patient Age", request.getAge(), errors);
        }
    }

    private void validateSmearTestResults(SmearTestResults smearTestResults, List<String> errors, SmearTestResultRequests smearTestResultRequest) {
        if (smearTestResultRequest.getAll().size() == 0) {
            if (smearTestResults.size() == 0) {
                return;
            } else {
                errors.add("No smear test results found in data file. But found following smear test results in db");
                for (int i = 0; i < smearTestResults.size(); i++)
                    errors.add("Found smear test result for type " + smearTestResults.get(0).getSmear_sample_instance());
                return;
            }
        }

        for (SmearTestSampleInstance instance : SmearTestSampleInstance.values()) {
            SmearTestResultRequests.SmearTestResultRequest expectedRecord = smearTestResultRequest.getSmearTestInstanceRecord(instance);
            SmearTestRecord actualRecord = getSmearTestRecord(smearTestResults, instance);
            validateSmearTestRecord(expectedRecord, actualRecord, instance, errors);
        }
    }

    private void validateWeightStatistics(ImportPatientRequest request, List<String> errors, WeightStatistics weightStatistics) {
        WeightStatisticsRequests weightStatisticsRequest = request.getWeightStatisticsRequest();
        if (weightStatisticsRequest.getAll().size() == 0) {
            if (weightStatistics.size() == 0) {
                return;
            } else {
                errors.add("No smear test results found in data file. But found following smear test results in db");
                for (int i = 0; i < weightStatistics.size(); i++)
                    errors.add("Found smear test result for type " + weightStatistics.get(0).getWeight_instance());
                return;
            }
        }

        for (WeightInstance instance : WeightInstance.values()) {
            WeightStatisticsRequests.WeightStatisticsRequest expectedRecord = weightStatisticsRequest.getWeightStatisticsRecord(instance);
            WeightStatisticsRecord actualRecord = getWeightStatisticsRecord(weightStatistics, instance);
            validateWeightStatisticsRecord(request,expectedRecord, actualRecord, instance, errors);
        }

    }

    private void validateSmearTestRecord(SmearTestResultRequests.SmearTestResultRequest expectedRecord, SmearTestRecord actualRecord, SmearTestSampleInstance instanceType, List<String> errors) {
        String date1FieldName = "Date1 of SmearTestResult type " + instanceType.name();
        String date2FieldName = "Date2 of SmearTestResult type " + instanceType.name();
        String result1FieldName = "Result1 of SmearTestResult typ;e " + instanceType.name();
        String result2FieldName = "Result2 of SmearTestResult type " + instanceType.name();

        if (expectedRecord == null) {
            if (isBlank(expectedRecord.getDate1()) && isBlank(expectedRecord.getDate2()) && isBlank(expectedRecord.getResult1()) && isBlank(expectedRecord.getResult2()))
                return;
            else {
                checkIfEqual(expectedRecord.getDate1(), null, date1FieldName, errors);
                checkIfEqual(expectedRecord.getDate2(), null, date2FieldName, errors);
                checkIfEqual(expectedRecord.getResult1(), null, result1FieldName, errors);
                checkIfEqual(expectedRecord.getResult2(), null, result2FieldName, errors);
            }
        }

        if (hasText(expectedRecord.getDate1()))
            checkIfDatesAreEqual(expectedRecord.getDate1(), actualRecord.getSmear_test_date_1(), date1FieldName, errors, LocalDate.class);

        if (hasText(expectedRecord.getDate2()))
            checkIfDatesAreEqual(expectedRecord.getDate2(), actualRecord.getSmear_test_date_2(), date2FieldName, errors, LocalDate.class);

        if (hasText(expectedRecord.getResult1()))
            checkIfEnumsAreEqual(expectedRecord.getResult1(), actualRecord.getSmear_test_result_1(), result1FieldName, errors, SmearTestResult.class);

        if (hasText(expectedRecord.getResult2()))
            checkIfEnumsAreEqual(expectedRecord.getResult2(), actualRecord.getSmear_test_result_2(), result2FieldName, errors, SmearTestResult.class);
    }

    private void validateWeightStatisticsRecord(ImportPatientRequest request, WeightStatisticsRequests.WeightStatisticsRequest expectedRecord, WeightStatisticsRecord actualRecord, WeightInstance instanceType, List<String> errors) {
        String weightDateFieldName = "Measuring Date of WeightStatistics type " + instanceType.name();
        String weightFieldName = "Weight of WeightStatistics type " + instanceType.name();

        if (actualRecord == null) {
            if (isBlank(expectedRecord.getWeight()))
                return;
            else {
                checkIfEqual(expectedRecord.getWeightDate(), null, weightDateFieldName, errors);
                checkIfEqual(expectedRecord.getWeight(), null, weightFieldName, errors);
            }
        }

        checkIfDatesAreEqual(request.getDate_modified(), actualRecord.getMeasuringDate(),weightFieldName,errors,LocalDate.class);

        if (hasText(expectedRecord.getWeight())) {
            try {
                checkIfEqual(Double.parseDouble(expectedRecord.getWeight()), actualRecord.getWeight(), weightFieldName, errors);
            } catch (NumberFormatException exception) {
                checkIfEqual(expectedRecord.getWeight(), actualRecord.getWeight().toString(), weightFieldName, errors);
            }
        }
    }

    private SmearTestRecord getSmearTestRecord(SmearTestResults smearTestResults, SmearTestSampleInstance instanceType) {
        for (int i = 0; i < smearTestResults.size(); i++) {
            if (smearTestResults.get(i).getSmear_sample_instance().equals(instanceType))
                return smearTestResults.get(i);
        }
        return null;
    }

    private WeightStatisticsRecord getWeightStatisticsRecord(WeightStatistics weightStatistics, WeightInstance instanceType) {
        for (int i = 0; i < weightStatistics.size(); i++) {
            if (weightStatistics.get(i).getWeight_instance().equals(instanceType))
                return weightStatistics.get(i);
        }
        return null;
    }

    private void checkIfEnumsAreEqual(String requestEnum, Object patientEnum, String fieldName, List<String> errors, Class enumClass) {
        if (isBlank(requestEnum)) {
            checkIfEqual(null, patientEnum, fieldName, errors);
            return;
        }

        Object expectedEnum = stringToEnumeration.convert(requestEnum, enumClass);
        checkIfEqual(expectedEnum, patientEnum, fieldName, errors);
    }

    private void checkIfDatesAreEqual(String requestDate, Object patientDate, String fieldName, List<String> errors, Class dateClass) {
        try {
            if (isBlank(requestDate)) {
                checkIfEqual(null, patientDate, fieldName, errors);
                return;
            }

            if (dateClass == DateTime.class) {
                String expectedDate = ((DateTime) stringToDateTime.convert(requestDate, dateClass)).toString(WHPConstants.DATE_TIME_FORMAT);
                checkIfEqual(expectedDate, ((DateTime) patientDate).toString(WHPConstants.DATE_TIME_FORMAT), fieldName, errors);
            } else {
                String expectedDate = ((LocalDate) stringToDateTime.convert(requestDate, dateClass)).toString(WHPConstants.DATE_FORMAT);
                checkIfEqual(expectedDate, ((LocalDate) patientDate).toString(WHPConstants.DATE_FORMAT), fieldName, errors);
            }
        } catch (Exception e) {
            printInvalidDataError(fieldName, errors, requestDate);
        }
    }

    private void printInvalidDataError(String fieldName, List<String> errors, String data) {
        errors.add("Invalid data provided for \"" + fieldName + "\": " + data + "\"");
    }

    private void checkIfEqual(Object expected, Object actual, String fieldName, List<String> errors) {
        if (expected != null && !expected.equals(actual))
            errors.add(fieldName + " should be set as \"" + expected + "\" but is set as \"" + actual + "\"");
        else if (expected == null && actual != null)
            errors.add(fieldName + " should be set as NULL but is set as \"" + actual + "\"");
    }

    private void addNotSetError(String fieldName, String expectedValue, List<String> errors) {
        errors.add(fieldName + " is not set. To be set as \"" + expectedValue + "\"");
    }
}

