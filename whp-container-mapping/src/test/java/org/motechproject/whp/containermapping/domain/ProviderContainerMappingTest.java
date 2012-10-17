package org.motechproject.whp.containermapping.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class ProviderContainerMappingTest {

    @Test
    public void shouldAddContainerMapping(){
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        ContainerRange containerRange = new ContainerRange(100, 200);
        providerContainerMapping.add(containerRange);

        assertThat(providerContainerMapping.getContainerRanges(), hasItem(containerRange));
    }
}
