package org.motechproject.whp.container.mapping.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.whp.container.mapping.domain.AdminContainerMapping;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@ContextConfiguration(locations = "classpath*:/applicationContainerMappingContext.xml")
public class AllAdminContainerMappingsIT extends SpringIntegrationTest {

    @Autowired
    AllAdminContainerMappings allAdminContainerMappings;

    @Autowired
    @Qualifier("whpDbConnector")
    CouchDbConnector couchDbConnector;

    private AdminContainerMapping adminContainerMapping;

    @Before
    public void setUp() {
        adminContainerMapping = new AdminContainerMapping();
        adminContainerMapping.add(new ContainerRange(100, 199))
                .add(new ContainerRange(200, 299))
                .add(new ContainerRange(300, 399));

    }

    @Test
    public void shouldAddAdminContainerMapping(){
        addAndMarkForDeletion(adminContainerMapping);

        assertThat(allAdminContainerMappings.getAll(), hasItem(adminContainerMapping));
        assertEquals(3, allAdminContainerMappings.get(adminContainerMapping.getId()).getContainerRanges().size());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

    private void addAndMarkForDeletion(AdminContainerMapping adminContainerMapping) {
        allAdminContainerMappings.add(adminContainerMapping);
        markForDeletion(adminContainerMapping);
    }
}
