package org.motechproject.whp.controller.it;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.motechproject.whp.container.mapping.repository.AllProviderContainerMappings;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.motechproject.whp.webservice.service.ProviderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class ProviderContainerRegistrationControllerIT extends SpringIntegrationTest {

    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;

    @Autowired
    ProviderWebService providerWebService;

    private final String providerId = "provider";

    @Before
    public void setUp() {
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(10000000000L, 20000000000L));
        providerContainerMapping.setProviderId(providerId);

        allProviderContainerMappings.add(providerContainerMapping);

        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().withProviderId(providerId).build();
    }

    @Test
    public void shouldRegisterContainer(){

    }


}
