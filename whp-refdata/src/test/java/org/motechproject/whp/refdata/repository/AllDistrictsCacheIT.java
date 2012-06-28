package org.motechproject.whp.refdata.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.objectcache.AllDistrictsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationRefDataContext.xml")
public class AllDistrictsCacheIT extends SpringIntegrationTest {

    @Autowired
    AllDistricts allDistricts;

    @Autowired
    AllDistrictsCache allDistrictsCache;

    private District districtA;
    private District districtB;

    @Before
    public void setUp() {
        districtA = new District("districtA");
        districtB = new District("districtB");
    }
    @After
    public void tearDown() {
        allDistricts.remove(districtA);
        allDistricts.remove(districtB);
        allDistrictsCache.refresh();
    }

    @Test
    public void shouldLoadAllDistricts() {
        int sizeBefore = allDistrictsCache.getAll().size();
        allDistricts.add(districtA);
        allDistricts.add(districtB);

        allDistrictsCache.refresh();

        List<District> districts = allDistrictsCache.getAll();
        assertEquals(sizeBefore + 2, districts.size());
        assertTrue(districts.contains(new District("districtB")));
        assertTrue(districts.contains(new District("districtA")));
    }

    @Test
    public void shouldLoadAllDistrictsInAscendingOrder() {
        int sizeBefore = allDistrictsCache.getAll().size();
        allDistricts.add(districtB);
        allDistricts.add(districtA);

        allDistrictsCache.refresh();

        List<District> districts = allDistrictsCache.getAll();
        assertEquals(sizeBefore + 2, districts.size());
        assertTrue(districts.indexOf(new District("districtA")) < districts.indexOf(new District("districtB")));
    }

    @Test
    public void shouldFetchDistrictById() {
        allDistricts.add(districtB);
        allDistricts.add(districtA);

        allDistrictsCache.refresh();

        assertEquals(districtA, allDistrictsCache.getBy(districtA.getId()));
    }

}
