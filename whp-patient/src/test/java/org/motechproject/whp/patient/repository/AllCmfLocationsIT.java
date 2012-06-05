package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.CmfLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllCmfLocationsIT extends SpringIntegrationTest{

    @Autowired
    AllCmfLocations allCmfLocations;
    @Before
    @After
    public void setup() {
       allCmfLocations.removeAll();
    }

    @Test
    public void shouldStoreAndFindLocationById() {
       allCmfLocations.addOrReplace(new CmfLocation("Delhi"));
       assertEquals(1,allCmfLocations.getAll().size());
    }

    @Test
    public void shouldReturnNullIfIdIsNull() {
       assertNull(allCmfLocations.findByLocation(null));
    }
    @Test
    public void shouldFindByLocation() {
        allCmfLocations.addOrReplace(new CmfLocation("Delhi"));
        assertNotNull(allCmfLocations.findByLocation("Delhi"));
    }
}
