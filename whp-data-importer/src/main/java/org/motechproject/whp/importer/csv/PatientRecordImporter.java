package org.motechproject.whp.importer.csv;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerBeanMapper;
import org.joda.time.LocalDate;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.mapping.StringToDateTime;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@CSVImporter(entity = "patientRecordImporter", bean = ImportPatientRequest.class)
@Component
public class PatientRecordImporter {

    private RegistrationService registrationService;
    private RequestValidator validator;

    private DozerBeanMapper importPatientRequestMapper;

    private StringToDateTime stringToDateTime = new StringToDateTime();

    @Autowired
    public PatientRecordImporter(RegistrationService registrationService, RequestValidator validator, DozerBeanMapper importPatientRequestMapper) {
        this.registrationService = registrationService;
        this.importPatientRequestMapper = importPatientRequestMapper;
        this.validator = validator;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        for (int i=0;i<objects.size();i++) {
            try {
                ImportPatientRequest request = (ImportPatientRequest) objects.get(i);
                validator.validate(request, UpdateScope.createScope);
                setDefaultValuesIfEmpty(request);
            } catch (Exception e) {
                System.out.println(String.format("Exception thrown for object in row %d, with case id - %s", i + 1, ((ImportPatientRequest) objects.get(i)).getCase_id()));
                System.out.println(e.getMessage());
                System.out.println();
                return false;
            }
        }
        return true;
    }


    private void setDefaultValuesIfEmpty(ImportPatientRequest request) {
        if(StringUtils.isBlank(request.getWeight_date()))
        {
            LocalDate registrationDate = (LocalDate) stringToDateTime.convert(request.getDate_modified(), LocalDate.class);
            request.setWeight_date(registrationDate.toString("dd/MM/YYYY"));
        }
        if(StringUtils.isBlank(request.getPatient_type()))
            request.setPatient_type(PatientType.New.name());
    }

    @Post
    public void post(List<Object> objects) {
        System.out.println("Number of patient records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportPatientRequest importPatientRequest = (ImportPatientRequest) object;
                System.out.println("Storing patient with patient id :" + importPatientRequest.getCase_id());
                importPatientRequest.setMigrated(true);
                registerPatient(importPatientRequest);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerPatient(ImportPatientRequest importPatientRequest) {
        PatientRequest patientRequest = importPatientRequestMapper.map(importPatientRequest, PatientRequest.class);
        registrationService.registerPatient(patientRequest);
    }

}

