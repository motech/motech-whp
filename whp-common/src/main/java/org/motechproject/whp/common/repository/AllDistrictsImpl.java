package org.motechproject.whp.common.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.crud.repository.CouchDBCrudRepository;
import org.motechproject.whp.common.domain.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository(value = "allDistricts")
public class AllDistrictsImpl extends CouchDBCrudRepository<District> implements AllDistricts {

    public static final String DISTRICTS_CACHE_NAME = "districts";
    private CacheManager districtsCacheManager;

    @Autowired
    public AllDistrictsImpl(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector,
                            CacheManager districtsCacheManager) {
        super(District.class, dbCouchDbConnector);
        this.districtsCacheManager = districtsCacheManager;
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

    public void refreshCache() {
        //programmatically evicting cache for self-referential scenarios
        districtsCacheManager.getCache(DISTRICTS_CACHE_NAME).evict(0);
    }


    public void add(District district) {
        super.add(district);
        refreshCache();
    }

    public void remove(District district) {
        super.remove(district);
        refreshCache();
    }

}
