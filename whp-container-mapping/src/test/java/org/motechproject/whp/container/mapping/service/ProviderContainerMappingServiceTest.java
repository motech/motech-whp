package org.motechproject.whp.container.mapping.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.motechproject.whp.container.mapping.repository.AllProviderContainerMappings;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderContainerMappingServiceTest {

    @Mock
    private AllProviderContainerMappings allProviderContainerMappings;
    private ProviderContainerMappingService providerContainerMappingService;

    @Before
    public void setUp() {
        initMocks(this);
        providerContainerMappingService = new ProviderContainerMappingService(allProviderContainerMappings);
    }

    @Test
    public void shouldAddAProviderContainerMapping() {
        ProviderContainerMapping containerMapping = mock(ProviderContainerMapping.class);

        providerContainerMappingService.add(containerMapping);

        verify(allProviderContainerMappings).add(containerMapping);
    }

    @Test
    public void shouldReturnTrueIfContainerIdFallsWithinTheCorrectRangeOfProvider() {
        String providerId = "providerId";
        String containerId = "1234";
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        ArrayList<ContainerRange> containerRanges = new ArrayList<>();
        containerRanges.add(new ContainerRange(1000, 2000));
        providerContainerMapping.setContainerRanges(containerRanges);

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(providerContainerMapping);

        assertTrue(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }

    @Test
    public void shouldReturnFalseIfContainerIdFallsOutsideTheCorrectRangeOfProvider() {
        String providerId = "providerId";
        String containerId = "123";
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        ArrayList<ContainerRange> containerRanges = new ArrayList<>();
        containerRanges.add(new ContainerRange(1000, 2000));
        providerContainerMapping.setContainerRanges(containerRanges);

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(providerContainerMapping);

        assertFalse(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }
}
