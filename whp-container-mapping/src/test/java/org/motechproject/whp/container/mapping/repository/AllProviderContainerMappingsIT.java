package org.motechproject.whp.container.mapping.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@ContextConfiguration(locations = "classpath*:/applicationContainerMappingContext.xml")
public class AllProviderContainerMappingsIT extends SpringIntegrationTest{

    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;

    @Autowired
    @Qualifier("whpDbConnector")
    CouchDbConnector couchDbConnector;

    private ProviderContainerMapping providerContainerMapping;
    private String providerId = "providerId";

    @Before
    public void setUp() {
        providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.setProviderId(providerId);
        providerContainerMapping.add(new ContainerRange(100, 199))
                   .add(new ContainerRange(200, 299))
                   .add(new ContainerRange(300, 399));

    }

    @Test
    public void shouldAddProviderContainerMapping(){
        allProviderContainerMappings.add(providerContainerMapping);

        assertThat(allProviderContainerMappings.getAll(), hasItem(providerContainerMapping));
        assertEquals(3, allProviderContainerMappings.get(providerContainerMapping.getId()).getContainerRanges().size());

        markForDeletion(providerContainerMapping);
    }


    @Override
    public CouchDbConnector getDBConnector() {
     return couchDbConnector;
    }
}
