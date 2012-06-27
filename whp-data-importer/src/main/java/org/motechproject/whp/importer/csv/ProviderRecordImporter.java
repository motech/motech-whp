package org.motechproject.whp.importer.csv;

import org.joda.time.DateTime;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "providerRecordImporter", bean = ImportProviderRequest.class)
@Component
public class ProviderRecordImporter {

    private ProviderService providerService;
    private RequestValidator validator;
    private AllProviders allProviders;
    private ImporterLogger importerLogger = new ImporterLogger();

    @Autowired
    public ProviderRecordImporter(AllProviders allProviders, ProviderService providerService, RequestValidator validator) {
        this.allProviders = allProviders;
        this.providerService = providerService;
        this.validator = validator;
    }

    @Validate
    public ValidationResponse validate(List<Object> objects) {
        boolean isValid = true;
        ArrayList<String> providerIdList = new ArrayList<String>();
        for (int i = 0; i < objects.size(); i++) {
            ImportProviderRequest request = (ImportProviderRequest) objects.get(i);

            try {
                validator.validate(request, UpdateScope.createScope);
                validateProviderIdIsUnique(providerIdList, request.getProviderId());
                validateIfProviderWithIdExistsInDb(request.getProviderId());
            } catch (Exception e) {
                String errorMessage = String.format("Exception thrown for object in row %d, with provider id - %s", i + 1, ((ImportProviderRequest) objects.get(i)).getProviderId()) +
                        "\n" + e.getMessage() + "\n";
                importerLogger.error(errorMessage);
                isValid = false;
            }
            providerIdList.add(request.getProviderId());

        }
        return new ValidationResponse(isValid);
    }

    private void validateProviderIdIsUnique(ArrayList<String> providerIdList, String providerId) throws WHPImportException {
        if (providerIdList.contains(providerId)) {
            throw new WHPImportException("A provider with the same provider id already exists in the given data.");
        }
    }

    private void validateIfProviderWithIdExistsInDb(String providerId) throws WHPImportException {
        if (allProviders.findByProviderId(providerId) != null) {
            throw new WHPImportException(WHPErrorCode.DUPLICATE_PROVIDER_ID.getMessage());
        }
    }

    @Post
    public void post(List<Object> objects) {
        importerLogger.info("Number of provider records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ImportProviderRequest importProviderRequest = (ImportProviderRequest) object;
                importerLogger.info("Storing provider with provider id :" + importProviderRequest.getProviderId());
                registerProvider(importProviderRequest);
            } catch (Exception exception) {
                importerLogger.error(exception);
            }
        }
    }

    public void registerProvider(ImportProviderRequest importProviderRequest) {
        providerService.registerProvider(mapToProviderRequest(importProviderRequest));
        providerService.activateUser(importProviderRequest.getProviderId().toLowerCase());
    }

    private ProviderRequest mapToProviderRequest(ImportProviderRequest importProviderRequest) {
        ProviderRequest providerRequest = new ProviderRequest(importProviderRequest.getProviderId(), importProviderRequest.getDistrict(), importProviderRequest.getPrimaryMobile(), DateTime.now());
        providerRequest.setSecondaryMobile(importProviderRequest.getSecondaryMobile());
        providerRequest.setTertiaryMobile(importProviderRequest.getTertiaryMobile());
        return providerRequest;
    }

}

