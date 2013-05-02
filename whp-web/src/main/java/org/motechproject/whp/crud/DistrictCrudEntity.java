package org.motechproject.whp.crud;

import org.motechproject.crud.service.CouchDBCrudEntity;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class DistrictCrudEntity extends CouchDBCrudEntity<District> {

    @Autowired
    public DistrictCrudEntity(AllDistricts allDistricts) {
        super(allDistricts);
    }

    @Override
    public List<String> getDisplayFields() {
        return asList("name");
    }

    @Override
    public List<String> getFilterFields() {
        return asList("name");
    }

    @Override
    public Class getEntityType() {
        return District.class;
    }
}
