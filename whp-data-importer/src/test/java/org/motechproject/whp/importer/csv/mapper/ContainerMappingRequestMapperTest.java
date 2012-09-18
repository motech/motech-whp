package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ContainerMappingRequestMapperTest {

    @Test
    public void shouldMapContainerMappingRequestsToProviderContainerMapping(){
        ContainerMappingRequest request = new ContainerMappingRequest("1", "101", "200");

        ContainerMappingRequestMapper containerMappingRequestMapper = new ContainerMappingRequestMapper();

        ProviderContainerMapping mapping = containerMappingRequestMapper.map(request);

        assertThat(mapping.getProviderId(), is("1"));
        assertThat(mapping.getContainerRanges().get(0), is(new ContainerRange(101, 200)));
        assertThat(mapping.getContainerRanges().size(), is(1));
    }
}
