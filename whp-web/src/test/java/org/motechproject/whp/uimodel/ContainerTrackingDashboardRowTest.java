package org.motechproject.whp.uimodel;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;

import static org.junit.Assert.assertEquals;

public class ContainerTrackingDashboardRowTest {

    private DateTime now = DateUtil.now();

    @Test
    public void shouldReturnActionAsOpenForContainerWithStatusAsClosed() {
        ContainerTrackingDashboardRow containerTrackingDashboardRow = new ContainerTrackingDashboardRow();
        containerTrackingDashboardRow.setContainerStatus(ContainerStatus.Open.name());

        assertEquals("Close", containerTrackingDashboardRow.getAction());
    }

    @Test
    public void shouldReturnActionAsCloseForContainerWithStatusAsOpen() {
        ContainerTrackingDashboardRow containerTrackingDashboardRow = new ContainerTrackingDashboardRow();
        containerTrackingDashboardRow.setContainerStatus(ContainerStatus.Closed.name());

        assertEquals("Open", containerTrackingDashboardRow.getAction());
    }
}
