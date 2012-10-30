package org.motechproject.whp.containerregistration.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerRegistrationRequestMapperTest {

    @Mock
    ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldMapIvrContainerRegistrationRequestToContainerRegistrationRequest() {

        Provider provider = new Provider("providerId", "primaryMobile", "district", null);
        when(providerService.findByMobileNumber("msisdn")).
                thenReturn(provider);
        IvrContainerRegistrationRequest ivrContainerRegistrationRequest = new IvrContainerRegistrationRequest();
        ivrContainerRegistrationRequest.setCall_id("call_id");
        ivrContainerRegistrationRequest.setContainer_id("container_id");
        ivrContainerRegistrationRequest.setMsisdn("msisdn");
        ivrContainerRegistrationRequest.setPhase("phase");

        ContainerRegistrationRequestMapper containerRegistrationRequestMapper = new ContainerRegistrationRequestMapper(providerService);
        ContainerRegistrationRequest containerRegistrationReportingRequest = containerRegistrationRequestMapper.buildContainerRegistrationRequest(ivrContainerRegistrationRequest);

        verify(providerService,times(1)).findByMobileNumber("msisdn");
        assertEquals("container_id", containerRegistrationReportingRequest.getContainerId());
        assertEquals("phase", containerRegistrationReportingRequest.getInstance());
        assertEquals(provider.getProviderId(), containerRegistrationReportingRequest.getProviderId());
        assertEquals(ChannelId.IVR.name(), containerRegistrationReportingRequest.getChannelId());

    }
}
