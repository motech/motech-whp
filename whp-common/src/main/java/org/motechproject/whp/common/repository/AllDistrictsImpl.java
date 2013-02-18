package org.motechproject.whp.common.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.domain.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository(value = "allDistricts")
public class AllDistrictsImpl extends MotechBaseRepository<District> implements AllDistricts {

    public static final String DISTRICTS_CACHE_NAME = "districts";

    @Autowired
    public AllDistrictsImpl(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(District.class, dbCouchDbConnector);
    }

    @Cacheable(value = DISTRICTS_CACHE_NAME)
    public List<District> getAll() {
        List<District> districts = super.getAll();
        Collections.sort(districts);
        return districts;
    }

    @Override
    @GenerateView
    public District findByName(String name) {
        if (name == null)
            return null;
        ViewQuery find_by_name = createQuery("by_name").key(name).includeDocs(true);
        return singleResult(db.queryView(find_by_name, District.class));
    }

    @CacheEvict(value = DISTRICTS_CACHE_NAME)
    public void refresh() {
    }


    @CacheEvict(value = DISTRICTS_CACHE_NAME, allEntries = true)
    public void add(District district) {
        super.add(district);
    }

    @CacheEvict(value = DISTRICTS_CACHE_NAME, allEntries = true)
    public void remove(District district) {
        super.remove(district);
    }

}
