package org.motechproject.whp.refdata.objectcache;

import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllDistrictsCache extends ObjectCache<District> {

    @Autowired
    public AllDistrictsCache(AllDistricts allDistricts) {
        super(allDistricts, District.class);
    }
}

