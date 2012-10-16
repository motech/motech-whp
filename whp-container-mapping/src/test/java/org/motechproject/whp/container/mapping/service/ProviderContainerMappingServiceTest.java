package org.motechproject.whp.container.mapping.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.motechproject.whp.container.mapping.repository.AllProviderContainerMappings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
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
    public void shouldAddTheContainerRangeToProviderContainerMappingForGivenProviderId() {
        String providerId = "providerId";
        String containerId = "12345";
        ProviderContainerMapping mapping = new ProviderContainerMapping();
        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(mapping);

        providerContainerMappingService.addRange(providerId, containerId);

        verify(allProviderContainerMappings).findByProviderId(providerId);
        ArgumentCaptor<ProviderContainerMapping> captor = ArgumentCaptor.forClass(ProviderContainerMapping.class);
        verify(allProviderContainerMappings).update(captor.capture());
        ProviderContainerMapping actualMapping = captor.getValue();

        assertTrue(actualMapping.getContainerRanges().hasContainerId(Long.parseLong(containerId)));
    }

    @Test
    public void shouldReturnTrueIfContainerIdFallsWithinTheCorrectRangeOfProvider() {
        String providerId = "providerId";
        String containerId = "1234";
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(1000, 2000));

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(providerContainerMapping);

        assertTrue(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }

    @Test
    public void shouldReturnFalseIfContainerIdFallsOutsideTheCorrectRangeOfProvider() {
        String providerId = "providerId";
        String containerId = "123";
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(1000, 2000));

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(providerContainerMapping);

        assertFalse(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }

    @Test
    public void shouldReturnFalseWhenThereAreNoContainerMappingsForProvider() {
        String providerId = "providerId";
        String containerId = "123";
        ProviderContainerMapping providerContainerMapping = mock(ProviderContainerMapping.class);

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(providerContainerMapping);

        assertFalse(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }

    @Test
    public void shouldReturnFalseWhenProviderDoesNotExists() {
        String providerId = "providerId";
        String containerId = "123";

        when(allProviderContainerMappings.findByProviderId(providerId)).thenReturn(null);

        assertFalse(providerContainerMappingService.isValidContainerForProvider(providerId, containerId));
    }
}
