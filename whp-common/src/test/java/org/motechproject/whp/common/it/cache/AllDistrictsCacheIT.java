package org.motechproject.whp.common.it.cache;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.kubek2k.springockito.annotations.WrapWithSpy;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationCommonContext.xml")
public class AllDistrictsCacheIT extends SpringIntegrationTest {

    @Autowired
    @Qualifier(value = "allDistricts")
    AllDistricts allDistricts;

    @Autowired
    @Qualifier("whpDbConnector")
    @WrapWithSpy
    CouchDbConnector whpDbConnector;

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
        allDistricts.refresh();
        reset(whpDbConnector);
    }

    @Test
    public void shouldLoadFromCache() {
        allDistricts.add(districtA);
        allDistricts.add(districtB);

        allDistricts.getAll();
        allDistricts.getAll();
        verify(whpDbConnector, times(1)).queryView((ViewQuery) any(), eq(District.class));
    }

    @Test
    public void shouldRefreshDistricts() {
        int sizeBefore = allDistricts.getAll().size();
        allDistricts.add(districtA);
        allDistricts.add(districtB);

        allDistricts.refresh();

        List<District> districts = allDistricts.getAll();
        assertEquals(sizeBefore + 2, districts.size());
        assertTrue(districts.contains(new District("districtB")));
        assertTrue(districts.contains(new District("districtA")));
    }

    @Test
    public void shouldLoadAllDistrictsInAscendingOrder() {
        allDistricts.add(districtB);
        allDistricts.add(districtA);

        List<District> districts = allDistricts.getAll();
        assertTrue(districts.indexOf(new District("districtA")) < districts.indexOf(new District("districtB")));
    }
}
