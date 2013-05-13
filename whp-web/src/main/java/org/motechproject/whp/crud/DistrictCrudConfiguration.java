package org.motechproject.whp.crud;

import org.motechproject.crud.service.CrudEntity;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.motechproject.crud.builder.CrudEntityBuilder.newCrudEntity;
import static org.motechproject.crud.builder.CrudModelBuilder.couchDBCrudModel;

@Configuration
public class DistrictCrudConfiguration {

    @Bean(name = "districtCrudEntity")
    public CrudEntity createDistrictCrudEntity(AllDistricts allDistricts){
        return newCrudEntity(District.class)
                .crudRepository(allDistricts)
                .model(couchDBCrudModel(District.class)
                        .displayFields("name")
                        .filterFields("name")).build();
    }

}
