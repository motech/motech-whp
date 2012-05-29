package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "providerRecordValidator", bean = ImportProviderRequest.class)
@Component
public class ProviderRecordValidator {

    private AllProviders allProviders;

    @Autowired
    public ProviderRecordValidator(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @Validate
    public boolean validate(List<Object> objects) {
        return true;
    }

    @Post
    public void post(List<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            ImportProviderRequest request = (ImportProviderRequest) objects.get(i);
            List<String> errors = validateProvider(request);
            if (errors.size() > 0) {
                String errorMessage = String.format("Row %d: Provider with provider id \"%s\" has not been imported properly. These are the errors:\n", i + 1, request.getProviderId());
                for (String error : errors)
                    errorMessage += error + "\n";
                ImporterLogger.error(errorMessage);
            } else {
                ImporterLogger.info(String.format("Row %d: Provider with provider id \"%s\" has been imported properly", i + 1, request.getProviderId()));
            }
        }
    }

    private List<String> validateProvider(ImportProviderRequest request) {
        ArrayList<String> errors = new ArrayList<String>();
        Provider provider = allProviders.findByProviderId(request.getProviderId());
        if (provider == null) {
            errors.add("No provider found with provider id \"" + request.getProviderId() + "\"");
        } else {
            validateProviderFields(provider, request, errors);
        }
        return errors;
    }

    private void validateProviderFields(Provider provider, ImportProviderRequest request, ArrayList<String> errors) {
        checkIfEqual(request.getProviderId(), provider.getProviderId(), "ProviderId", errors);

        checkIfEqual(request.getDistrict(), provider.getDistrict(), "District", errors);
        checkIfEqual(request.getPrimaryMobile(), provider.getPrimaryMobile(), "PrimaryMobile", errors);
        checkIfEqual(request.getSecondaryMobile(), provider.getSecondaryMobile(), "SecondaryMobile", errors);
        checkIfEqual(request.getTertiaryMobile().toLowerCase(), provider.getTertiaryMobile(), "TertiaryMobile", errors);
    }

    private void checkIfEqual(Object expected, Object actual, String fieldName, List<String> errors) {
        if (expected != null && !expected.equals(actual))
            errors.add(fieldName + " should be set as \"" + expected + "\" but is set as \"" + actual + "\"");
    }

}

