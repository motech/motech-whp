package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@CSVImporter(entity = "containerMappingImporter", bean = ImportPatientRequest.class)
@Component
public class ContainerMappingImporter {

    private RequestValidator validator;
    private ImporterLogger importerLogger = new ImporterLogger();

    @Autowired
    public ContainerMappingImporter(RequestValidator validator) {
        this.validator = validator;
    }

    @Validate
    public ValidationResponse validate(List<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            ContainerMappingRequest request = (ContainerMappingRequest) objects.get(i);
            try {
                validator.validate(request, UpdateScope.createScope);
            } catch (Exception e) {
                String errorMessage = String.format("Validation error for object in row %d, with provider id - %s", i + 1,
                        ((ContainerMappingRequest) objects.get(i)).getProviderId()) +
                        "\n" + e.getMessage() + "\n";
                importerLogger.error(errorMessage);
                return new ValidationResponse(false);
            }
        }
        return new ValidationResponse(true);
    }
}
