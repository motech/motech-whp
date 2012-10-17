package org.motechproject.whp.containermapping.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;

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
        addAndMarkForDeletion(providerContainerMapping);

        assertThat(allProviderContainerMappings.getAll(), hasItem(providerContainerMapping));
        assertEquals(3, allProviderContainerMappings.get(providerContainerMapping.getId()).getContainerRanges().size());
    }

    @Test
    public void shouldFindProviderContainerMapping() {
        addAndMarkForDeletion(providerContainerMapping);

        ProviderContainerMapping containerMapping = allProviderContainerMappings.findByProviderId(providerId);

        assertNotNull(containerMapping);
        assertEquals(3, containerMapping.getContainerRanges().size());
        assertEquals(100, containerMapping.getContainerRanges().get(0).getFrom());
        assertEquals(199, containerMapping.getContainerRanges().get(0).getTo());
    }

    @Override
    public CouchDbConnector getDBConnector() {
     return couchDbConnector;
    }

    private void addAndMarkForDeletion(ProviderContainerMapping providerContainerMapping) {
        allProviderContainerMappings.add(providerContainerMapping);
        markForDeletion(providerContainerMapping);
    }
}
