package org.motechproject.whp.functional.test.sputumtracking;

import org.junit.BeforeClass;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestContainer;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.ContainerDashboardPage;
import org.motechproject.whp.functional.service.ContainerDataSeed;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class SputumTrackingDashboardTest extends BaseTest {

    private static List<TestContainer> testContainers;

    @BeforeClass
    public static void setupData() {
        ContainerDataSeed containerDataSeed = new ContainerDataSeed();
        testContainers = containerDataSeed.allContainers();
    }

    @Test
    public void shouldListAllRegisteredContainersWhenNoFiltersApplied() {
        for (TestContainer testContainer : testContainers) {
            ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
            assertTrue(page.hasContainerId(testContainer.getContainerId()));
            page.logout();
        }
    }
}


