package org.motechproject.whp.refdata.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.ContainerStatus;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;

public class ContainerStatusTest {

    @Test
    public void shouldReturnAllContainerStatusNames(){
        List<String> containerStatusNames = ContainerStatus.allNames();
        assertThat(containerStatusNames, hasItems(Open.name(), Closed.name()));
        assertThat(containerStatusNames.size(), is(ContainerStatus.values().length));
    }
}
