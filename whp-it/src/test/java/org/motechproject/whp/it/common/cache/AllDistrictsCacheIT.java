package org.motechproject.whp.it.common.cache;

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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationITContext.xml")
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

        allDistricts.add(districtA);
        allDistricts.add(districtB);
    }

    @After
    public void tearDown() {
        allDistricts.remove(districtA);
        allDistricts.remove(districtB);
        allDistricts.refreshCache();
        reset(whpDbConnector);
    }

    @Test
    public void shouldLoadFromCache() {
        allDistricts.getAll();
        allDistricts.getAll();
        verify(whpDbConnector, times(1)).queryView((ViewQuery) any(), eq(District.class));
    }

    @Test
    public void shouldRefreshDistricts() {
        allDistricts.getAll();

        allDistricts.refreshCache();

        allDistricts.getAll();

        verify(whpDbConnector, times(2)).queryView((ViewQuery) any(), eq(District.class));
    }

    @Test
    public void shouldRefreshDistrictsUponAddingAndRemovingDistrict() {
        allDistricts.getAll(); //cache values from DB
        allDistricts.getAll(); //load from cache

        District newDistrict = new District("someNewDistrict");
        allDistricts.add(newDistrict);

        List<District> districts = allDistricts.getAll(); //cache values from DB
        allDistricts.getAll(); //load from cache

        assertThat(districts, hasItem(newDistrict));

        allDistricts.remove(newDistrict);

        allDistricts.getAll(); //cache values from DB
        districts = allDistricts.getAll(); //load from cache

        assertThat(districts, not(hasItem(newDistrict)));
        verify(whpDbConnector, times(3)).queryView((ViewQuery) any(), eq(District.class));

    }

    @Test
    public void shouldLoadAllDistrictsInAscendingOrder() {
        List<District> districts = allDistricts.getAll();
        assertTrue(districts.indexOf(new District("districtA")) < districts.indexOf(new District("districtB")));
    }

    @Test
    public void shouldFindDistrictByName() {
        String district = "myOwnDistrict";
        allDistricts.add(new District(district));

        assertEquals(district, allDistricts.findByName(district).getName());
    }
}
