package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.mapper.ProviderRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.command.AllCommands;
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
    private ProviderRequestMapper providerRequestMapper;
    private AllProviders allProviders;


    @Autowired
    public ProviderRecordImporter(AllProviders allProviders,RegistrationService registrationService, RequestValidator validator,ProviderRequestMapper providerRequestMapper) {
        this.allProviders = allProviders;
        this.registrationService = registrationService;
        this.validator = validator;
        this.providerRequestMapper = providerRequestMapper;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        for (int i=0;i<objects.size();i++) {
            try {
                ImportProviderRequest request = (ImportProviderRequest) objects.get(i);
                validator.validate(request, AllCommands.create);
                if(allProviders.findByProviderId(request.getProviderId())!=null){
                    throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
                }
            } catch (Exception e) {
                System.out.println(String.format("Exception thrown for object in row %d, with provider id - %s", i + 1, ((ImportProviderRequest) objects.get(i)).getProviderId()));
                System.out.println(e.getMessage());
                System.out.println();
                return false;
            }
        }
        return true;
    }

    @Post
    public void post(List<Object> objects) {
        System.out.println("Number of provider records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportProviderRequest importProviderRequest = (ImportProviderRequest) object;
                System.out.println("Storing provider with provider id :" + importProviderRequest.getProviderId());
                registerProvider(importProviderRequest);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerProvider(ImportProviderRequest importProviderRequest) {
        ProviderRequest providerRequest = providerRequestMapper.map(importProviderRequest);
        registrationService.registerProvider(providerRequest);
    }

}

