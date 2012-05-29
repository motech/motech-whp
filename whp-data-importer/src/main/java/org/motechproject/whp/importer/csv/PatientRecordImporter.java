package org.motechproject.whp.importer.csv;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.mapper.ImportPatientRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.mapping.StringToDateTime;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "patientRecordImporter", bean = ImportPatientRequest.class)
@Component
public class PatientRecordImporter {

    private RegistrationService registrationService;
    private RequestValidator validator;
    private ImportPatientRequestMapper importPatientRequestMapper;
    private StringToDateTime stringToDateTime = new StringToDateTime();

    private AllPatients allPatients;

    @Autowired
    public PatientRecordImporter(RegistrationService registrationService,
                                 RequestValidator validator,
                                 ImportPatientRequestMapper importPatientRequestMapper,
                                 AllPatients allPatients) {
        this.registrationService = registrationService;
        this.importPatientRequestMapper = importPatientRequestMapper;
        this.validator = validator;
        this.allPatients = allPatients;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        boolean isValid = true;
        ArrayList<String> patientIds = new ArrayList<String>();
        for (int i = 0; i < objects.size(); i++) {
            ImportPatientRequest request = (ImportPatientRequest) objects.get(i);
            try {
                validator.validate(request, UpdateScope.createScope);
                validateCaseIdIsUnique(patientIds, request.getCase_id());
                validateIfPatientWithCaseIdExistsInDb(request.getCase_id());
                setDefaultValuesIfEmpty(request);
            } catch (Exception e) {
                String errorMessage = String.format("Exception thrown for object in row %d, with case id - %s", i + 1, ((ImportPatientRequest) objects.get(i)).getCase_id()) +
                        "\n" + e.getMessage() + "\n";
                ImporterLogger.error(errorMessage);
                isValid = false;
            }
            patientIds.add(request.getCase_id());
        }
        return isValid;
    }

    private void validateCaseIdIsUnique(ArrayList<String> patientIds, String case_id) throws WHPImportException {
        if (patientIds.contains(case_id)) {
            throw new WHPImportException("A patient with the case id already exists in the given data");
        }
    }

    private void validateIfPatientWithCaseIdExistsInDb(String case_id) throws WHPImportException {
        if (allPatients.findByPatientId(case_id) != null) {
            throw new WHPImportException(WHPErrorCode.DUPLICATE_CASE_ID.getMessage());
        }
    }

    private void setDefaultValuesIfEmpty(ImportPatientRequest request) {
        if (StringUtils.isBlank(request.getWeightDate(WeightInstance.PreTreatment))) {
            LocalDate registrationDate = (LocalDate) stringToDateTime.convert(request.getDate_modified(), LocalDate.class);
            request.setPreTreatmentWeightDate(registrationDate.toString(WHPConstants.DATE_FORMAT));
        }
        if (StringUtils.isBlank(request.getPatient_type()))
            request.setPatient_type(PatientType.New.name());
        if (StringUtils.isBlank(request.getPhi()))
            request.setPhi("WHP");
    }

    @Post
    public void post(List<Object> objects) {
        ImporterLogger.info("Number of patient records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportPatientRequest importPatientRequest = (ImportPatientRequest) object;
                ImporterLogger.info("Storing patient with patient id :" + importPatientRequest.getCase_id());
                importPatientRequest.setMigrated(true);
                registerPatient(importPatientRequest);
            } catch (Exception exception) {
                ImporterLogger.error(exception);
            }
        }
    }

    public void registerPatient(ImportPatientRequest importPatientRequest) {
        PatientRequest patientRequest = importPatientRequestMapper.map(importPatientRequest);
        registrationService.registerPatient(patientRequest);
    }
}

