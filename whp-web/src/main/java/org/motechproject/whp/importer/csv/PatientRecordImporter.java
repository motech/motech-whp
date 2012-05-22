package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.webservice.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@CSVImporter(entity = "patientWebRequest", bean = PatientWebRequest.class)
@Component
public class PatientRecordImporter {

    private PatientWebService patientWebService;
    private Properties whpAPIValidationProperty;
    private RequestValidator validator;

    @Autowired
    public PatientRecordImporter(PatientWebService patientWebService, @Qualifier("whpAPIValidationProperty") Properties whpAPIValidationProperty, RequestValidator validator) {
        this.patientWebService = patientWebService;
        this.whpAPIValidationProperty = whpAPIValidationProperty;
        this.validator = validator;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        // TODO case_id should be unique across the csv

        for (int i=0;i<objects.size();i++) {
            try {
                PatientWebRequest request = (PatientWebRequest) objects.get(i);
                request.setApi_key(whpAPIValidationProperty.getProperty("remedi.api.key"));
                validator.validate(request, ValidationScope.create);
            } catch (Exception e) {
                System.out.println(String.format("Exception thrown for object in row %d, with case id - %s",i+1,((PatientWebRequest)objects.get(i)).getCase_id()));
                System.out.println(e.getMessage());
                System.out.println();
                return false;
            }
        }
        return true;
    }

    @Post
    public void post(List<Object> objects) {
        for (Object object : objects) {
            try {
                PatientWebRequest patientWebRequest = (PatientWebRequest) object;
                patientWebService.createCase(patientWebRequest);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

