package org.motechproject.whp.importer.csv;

import org.joda.time.DateTime;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@CSVImporter(entity = "providerRecordImporter", bean = ImportProviderRequest.class)
@Component
public class ProviderRecordImporter {

    private RegistrationService registrationService;
    private RequestValidator validator;
    private AllProviders allProviders;


    @Autowired
    public ProviderRecordImporter(AllProviders allProviders,RegistrationService registrationService, RequestValidator validator) {
        this.allProviders = allProviders;
        this.registrationService = registrationService;
        this.validator = validator;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        boolean isValid = true;
        for (int i=0;i<objects.size();i++) {
            try {
                ImportProviderRequest request = (ImportProviderRequest) objects.get(i);
                validator.validate(request, UpdateScope.createScope);
                if(allProviders.findByProviderId(request.getProviderId())!=null){
                    throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
                }
            } catch (Exception e) {
                String errorMessage =  String.format("Exception thrown for object in row %d, with provider id - %s", i + 1, ((ImportProviderRequest) objects.get(i)).getProviderId()) +
                        "\n"+ e.getMessage() +"\n";
                ImporterLogger.error(errorMessage);
                isValid = false;
            }
        }
        return isValid;
    }

    @Post
    public void post(List<Object> objects) {
        ImporterLogger.info("Number of provider records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportProviderRequest importProviderRequest = (ImportProviderRequest) object;
                ImporterLogger.info("Storing provider with provider id :" + importProviderRequest.getProviderId());
                registerProvider(importProviderRequest);
            } catch (Exception exception) {
                ImporterLogger.error(exception);
            }
        }
    }

    public void registerProvider(ImportProviderRequest importProviderRequest) {
        registrationService.registerProvider(mapToProviderRequest(importProviderRequest));
    }

    private ProviderRequest mapToProviderRequest(ImportProviderRequest importProviderRequest) {
        ProviderRequest providerRequest = new ProviderRequest(importProviderRequest.getProviderId(), importProviderRequest.getDistrict(), importProviderRequest.getPrimaryMobile(), DateTime.now());
        providerRequest.setSecondaryMobile(importProviderRequest.getSecondaryMobile());
        providerRequest.setTertiaryMobile(importProviderRequest.getTertiaryMobile());
        return providerRequest;
    }

}

