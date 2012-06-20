package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "providerRecordValidator", bean = ImportProviderRequest.class)
@Component
public class ProviderRecordValidator {

    private AllProviders allProviders;
    private ImporterLogger importerLogger;

    @Autowired
    public ProviderRecordValidator(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @Validate
    public ValidationResponse validate(List<Object> objects) {
        return new ValidationResponse(true);
    }

    @Post
    public void post(List<Object> objects) {
        try {
            int objectsInDbCount = 0;
            for (int i = 0; i < objects.size(); i++) {
                ImportProviderRequest request = (ImportProviderRequest) objects.get(i);
                Provider provider = allProviders.findByProviderId(request.getProviderId());
                List<String> errors = new ArrayList<String>();

                if (provider == null) {
                    errors.add("No provider found with provider id \"" + request.getProviderId() + "\"");
                    continue;
                }

                objectsInDbCount++;
                validateProviderFields(provider, request, errors);

                if (errors.size() > 0) {
                    String errorMessage = String.format("Row %d: Provider with provider id \"%s\" has not been imported properly. These are the errors:\n", i + 1, request.getProviderId());
                    for (String error : errors)
                        errorMessage += error + "\n";
                    importerLogger.error(errorMessage);
                } else {
                    importerLogger.info(String.format("Row %d: Provider with provider id \"%s\" has been imported properly", i + 1, request.getProviderId()));
                }
            }
            importerLogger.info("Number of Objects found in the data file : " + objects.size());
            importerLogger.info("Number of Objects found in the db : " + objectsInDbCount);
        } catch (Exception exception) {
            importerLogger.error("Exception occured while testing...");
            importerLogger.error(exception);
        }
    }

    private void validateProviderFields(Provider provider, ImportProviderRequest request, List<String> errors) {
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

