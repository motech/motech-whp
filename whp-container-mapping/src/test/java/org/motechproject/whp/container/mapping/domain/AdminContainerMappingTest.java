package org.motechproject.whp.container.mapping.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

public class AdminContainerMappingTest {

    @Test
    public void shouldAddContainerMapping(){
        AdminContainerMapping adminContainerMapping = new AdminContainerMapping();
        ContainerRange containerRange = new ContainerRange(100, 200);
        adminContainerMapping.add(containerRange);

        assertThat(adminContainerMapping.getContainerRange(), is(containerRange));
    }
}
