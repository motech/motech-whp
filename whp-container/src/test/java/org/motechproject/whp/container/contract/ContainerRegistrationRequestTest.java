package org.motechproject.whp.container.contract;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ContainerRegistrationRequestTest {

    @Test
    public void shouldReturnProviderIdInLowerCase(){
        String providerId = "providerId";
        ContainerRegistrationRequest containerRegistrationRequest = new ContainerRegistrationRequest(providerId, "cid", "instance", "channelId", "callId");
        assertThat(containerRegistrationRequest.getProviderId(), is(providerId.toLowerCase()));
    }
    
    @Test
    public void shouldHandleNullProviderIdCases() {
        ContainerRegistrationRequest containerRegistrationRequestWithNullProviderId = new ContainerRegistrationRequest(null, "cid", "instance", "channelId", "callId");
        assertNull(containerRegistrationRequestWithNullProviderId.getProviderId());
    }
}
