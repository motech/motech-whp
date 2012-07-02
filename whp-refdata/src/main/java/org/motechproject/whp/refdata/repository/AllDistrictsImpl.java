package org.motechproject.whp.refdata.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.refdata.domain.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository(value = "allDistricts")
public class AllDistrictsImpl extends MotechBaseRepository<District> implements AllDistricts {

    @Autowired
    public AllDistrictsImpl(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(District.class, dbCouchDbConnector);
    }

    @Cacheable(value = "districts")
    public List<District> getAll() {
        List<District> districts = super.getAll();
        Collections.sort(districts);
        return districts;
    }

    @CacheEvict(value = "districts")
    public void refresh() {
    }

}
