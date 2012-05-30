package org.motechproject.whp.importer.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.whp.importer.csv.builder.ImportProviderRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderRecordImporterUnitTest {

    @Mock
    RequestValidator validator;
    ProviderRecordImporter providerRecordImporter;

    @Mock
    private RegistrationService registrationService;


    @Mock
    private AllProviders allProviders;

    @Before
    public void setup() {
        initMocks(this);
        providerRecordImporter = new ProviderRecordImporter(allProviders,registrationService, validator);
    }

    @Test
    public void shouldSaveAllProvidersDataEvenIfErrorOccurs() {
        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("1").build();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("2").build();

        doThrow(new RuntimeException("Exception to be thrown for test")).when(registrationService).registerProvider(Matchers.<ProviderRequest>any());

        providerRecordImporter.post(asList((Object) importProviderRequest1, importProviderRequest2));

        ArgumentCaptor<ProviderRequest> providerRequestArgumentCaptor = ArgumentCaptor.forClass(ProviderRequest.class);

        verify(registrationService, times(2)).registerProvider(providerRequestArgumentCaptor.capture());

        List<ProviderRequest> providerRequests = providerRequestArgumentCaptor.getAllValues();
        assertEquals(2, providerRequests.size());
        assertProviderRequest(importProviderRequest1, providerRequests.get(0));
        assertProviderRequest(importProviderRequest2, providerRequests.get(1));
    }

    private void assertProviderRequest(ImportProviderRequest importProviderRequest, ProviderRequest providerRequest) {
        assertEquals(importProviderRequest.getProviderId(), providerRequest.getProviderId());
        assertEquals(importProviderRequest.getDistrict(), providerRequest.getDistrict());
        assertEquals(importProviderRequest.getPrimaryMobile(), providerRequest.getPrimaryMobile());
        assertEquals(importProviderRequest.getSecondaryMobile(), providerRequest.getSecondaryMobile());
        assertEquals(importProviderRequest.getTertiaryMobile(), providerRequest.getTertiaryMobile());
    }

    @Test
    public void shouldReturnFalseIfInvalid() {
        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withProviderId("1").build();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withProviderId("2").build();
        doThrow(new RuntimeException("Exception to be thrown for test")).when(validator).validate(importProviderRequest2, UpdateScope.createScope);

        assertEquals(false, providerRecordImporter.validate(asList((Object) importProviderRequest1,importProviderRequest2)).isValid());
    }

    @Test
    public void shouldReturnFalseIfProviderAlreadyExists() {
        ImportProviderRequest importProviderRequest = new ImportProviderRequest();
        importProviderRequest.setProviderId("1");
        when(allProviders.findByProviderId("1")).thenReturn(new Provider());

        assertEquals(false, providerRecordImporter.validate(asList((Object) importProviderRequest)).isValid());
    }

    @Test
    public void shouldReturnTrueIfValid() {
        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("1").build();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("2").build();

        assertEquals(true, providerRecordImporter.validate(asList((Object) importProviderRequest1, importProviderRequest2)).isValid());
    }
    
    @Test
    public void shouldFailValidationIfDuplicateProviderIdsPresent() {

        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("1").build();
        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("1").build();

        assertFalse(providerRecordImporter.validate(asList((Object) importProviderRequest1, importProviderRequest2)).isValid());

    }
}
