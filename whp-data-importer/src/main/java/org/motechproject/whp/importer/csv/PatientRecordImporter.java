package org.motechproject.whp.importer.csv;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.mapper.ImportPatientRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.importer.csv.request.WeightStatisticsRequests;
import org.motechproject.whp.common.mapping.StringToDateTime;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.springframework.util.StringUtils.hasText;

@CSVImporter(entity = "patientRecordImporter", bean = ImportPatientRequest.class)
@Component
public class PatientRecordImporter {

    private PatientService patientService;
    private RequestValidator validator;
    private ImportPatientRequestMapper importPatientRequestMapper;
    private StringToDateTime stringToDateTime = new StringToDateTime();
    private ImporterLogger importerLogger = new ImporterLogger();
    private AllPatients allPatients;

    @Autowired
    public PatientRecordImporter(PatientService patientService,
                                 RequestValidator validator,
                                 ImportPatientRequestMapper importPatientRequestMapper,
                                 AllPatients allPatients) {
        this.patientService = patientService;
        this.importPatientRequestMapper = importPatientRequestMapper;
        this.validator = validator;
        this.allPatients = allPatients;
    }

    @Validate
    public ValidationResponse validate(List<Object> objects) {
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
                importerLogger.error(errorMessage);
                isValid = false;
            }
            patientIds.add(request.getCase_id());
        }
        return new ValidationResponse(isValid);
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
        if (StringUtils.isBlank(request.getWeightDate(SampleInstance.PreTreatment))) {
            LocalDate registrationDate = (LocalDate) stringToDateTime.convert(request.getDate_modified(), LocalDate.class);
            request.setPreTreatmentWeightDate(registrationDate.toString(DATE_FORMAT));
        }
        if (StringUtils.isBlank(request.getPatient_type()))
            request.setPatient_type(PatientType.New.name());
        if (StringUtils.isBlank(request.getPhi()))
            request.setPhi("WHP");

        for( WeightStatisticsRequests.WeightStatisticsRequest weightStatisticsRequest: request.getWeightStatisticsRequest().getAll())
            if(hasText(weightStatisticsRequest.getWeight()))
                weightStatisticsRequest.setWeightDate(request.getDate_modified());

        request.setDate_modified(request.getDate_modified() + " 00:00:00");
    }

    @Post
    public void post(List<Object> objects) {
        importerLogger.info("Number of patient records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportPatientRequest importPatientRequest = (ImportPatientRequest) object;
                importerLogger.info("Storing patient with patient id :" + importPatientRequest.getCase_id());
                importPatientRequest.setMigrated(true);
                registerPatient(importPatientRequest);
            } catch (Exception exception) {
                importerLogger.error(exception);
            }
        }
    }

    public void registerPatient(ImportPatientRequest importPatientRequest) {
        PatientRequest patientRequest = importPatientRequestMapper.map(importPatientRequest);
        patientService.createPatient(patientRequest);
    }
}

