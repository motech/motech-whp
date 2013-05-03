package org.motechproject.whp.crud;

import org.ektorp.CouchDbConnector;
import org.motechproject.crud.service.CrudEntity;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.motechproject.crud.builder.CrudEntityBuilder.newCrudEntity;
import static org.motechproject.crud.builder.CrudModelBuilder.couchDBCrudModel;

@Configuration
public class ProviderContainerMappingCrudConfiguration {

    @Bean
    public CrudEntity<ProviderContainerMapping> build(@Qualifier("whpDbConnector") CouchDbConnector couchDbConnector){
        return newCrudEntity(ProviderContainerMapping.class)
                .couchDBCrudRepository(couchDbConnector)
                .model(couchDBCrudModel(ProviderContainerMapping.class)
                        .displayFields("providerId")
                        .filterFields("providerId")).build();
    }
}
