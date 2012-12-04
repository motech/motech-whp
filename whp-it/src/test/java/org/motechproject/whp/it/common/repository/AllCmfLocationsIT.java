package org.motechproject.whp.it.common.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.domain.CmfLocation;
import org.motechproject.whp.common.repository.AllCmfLocations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllCmfLocationsIT extends SpringIntegrationTest {

    @Autowired
    AllCmfLocations allCmfLocations;

    @After
    public void setup() {
        allCmfLocations.removeAll();
    }

    @Test
    public void shouldStoreAndFindLocationById() {
        allCmfLocations.addOrReplace(new CmfLocation("Delhi"));
        assertEquals(1, allCmfLocations.getAll().size());
    }

    @Test
    public void shouldReturnNullIfIdIsNull() {
        assertNull(allCmfLocations.findByLocation(null));
    }

    @Test
    public void shouldFindByLocation() {
        allCmfLocations.add(new CmfLocation("Delhi"));
        assertEquals(1, allCmfLocations.getAll().size());
    }
}
