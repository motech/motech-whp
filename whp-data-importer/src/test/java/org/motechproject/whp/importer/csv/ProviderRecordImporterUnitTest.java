package org.motechproject.whp.importer.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.importer.csv.builder.ImportProviderRequestBuilder;
import org.motechproject.whp.importer.csv.mapper.ProviderRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderRecordImporterUnitTest {

    @Mock
    RequestValidator validator;
    ProviderRecordImporter providerRecordImporter;

    @Mock
    private RegistrationService registrationService;

    private ProviderRequestMapper providerRequestMapper;

    @Mock
    private AllProviders allProviders;

    @Before
    public void setup() {
        initMocks(this);
        providerRequestMapper = new ProviderRequestMapper();
        providerRecordImporter = new ProviderRecordImporter(allProviders,registrationService, validator, providerRequestMapper);
    }

    @Test
    public void shouldSaveAllProvidersDataEvenIfErrorOccurs() {
        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("1").build();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("2").build();

        ProviderRequest providerRequest1  = providerRequestMapper.map(importProviderRequest1);
        ProviderRequest providerRequest2  = providerRequestMapper.map(importProviderRequest2);

        doThrow(new RuntimeException("Exception to be thrown for test")).when(registrationService).registerProvider(providerRequest1);

        providerRecordImporter.post(asList((Object) importProviderRequest1, importProviderRequest2));

        verify(registrationService, times(1)).registerProvider(providerRequest1);
        verify(registrationService, times(1)).registerProvider(providerRequest2);
    }

    @Test
    public void shouldReturnFalseIfInvalid() {
        ImportProviderRequest importProviderRequest = new ImportProviderRequest();
        doThrow(new RuntimeException("Exception to be thrown for test")).when(validator).validate(any(), anyString());

        assertEquals(false, providerRecordImporter.validate(asList((Object) importProviderRequest)));
    }

    @Test
    public void shouldReturnFalseIfProviderAlreadyExists() {
        ImportProviderRequest importProviderRequest = new ImportProviderRequest();
        importProviderRequest.setProviderId("1");
        when(allProviders.findByProviderId("1")).thenReturn(new Provider());

        assertEquals(false, providerRecordImporter.validate(asList((Object) importProviderRequest)));
    }

    @Test
    public void shouldReturnTrueIfValid() {
        ImportProviderRequest importProviderRequest1 = new ImportProviderRequest();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequest();

        assertEquals(true, providerRecordImporter.validate(asList((Object) importProviderRequest1, importProviderRequest2)));
    }
}
