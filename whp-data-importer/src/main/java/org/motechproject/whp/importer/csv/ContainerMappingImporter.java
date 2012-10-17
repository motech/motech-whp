package org.motechproject.whp.importer.csv;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.ValidationResponse;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.importer.csv.mapper.ContainerMappingRequestMapper;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@CSVImporter(entity = "containerMappingImporter", bean = ContainerMappingRequest.class)
@Component
public class ContainerMappingImporter {

    private ImporterLogger importerLogger = new ImporterLogger();

    private RequestValidator validator;
    private ContainerMappingRequestMapper mapper;
    private ProviderContainerMappingService providerContainerMappingService;

    @Autowired
    public ContainerMappingImporter(RequestValidator validator, ContainerMappingRequestMapper containerMappingRequestMapper, ProviderContainerMappingService providerContainerMappingService) {
        this.validator = validator;
        this.mapper = containerMappingRequestMapper;
        this.providerContainerMappingService = providerContainerMappingService;
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

    @Post
    public void post(List<Object> objects) {
        importerLogger.info("Number of container mapping records to be stored in db :" + objects.size());
        for (Object object : objects) {
            try {
                ProviderContainerMapping providerContainerMapping = mapper.map((ContainerMappingRequest) object);
                providerContainerMappingService.add(providerContainerMapping);
                importerLogger.info("Storing container mapping with provider id :" + providerContainerMapping.getProviderId());
            } catch (Exception exception) {
                importerLogger.error(exception);
            }
        }
    }
}