package org.motechproject.whp.containermapping.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.containermapping.domain.AdminContainerMapping;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.repository.AllAdminContainerMappings;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdminContainerMappingServiceTest {

    @Mock
    AllAdminContainerMappings allAdminContainerMappings;

    AdminContainerMappingService adminContainerMappingService;

    @Before
    public void setUp() {
        initMocks(this);
        adminContainerMappingService = new AdminContainerMappingService(allAdminContainerMappings);
    }

    @Test
    public void shouldReturnTrueIfContainerIdFallsWithinTheCorrectRangeOfProvider() {
        AdminContainerMapping adminContainerMapping = new AdminContainerMapping();
        adminContainerMapping.add(new ContainerRange(1000, 2000));

        when(allAdminContainerMappings.get()).thenReturn(adminContainerMapping);

        assertTrue(adminContainerMappingService.isValidContainer(1234));
        assertFalse(adminContainerMappingService.isValidContainer(2234));
    }

}
