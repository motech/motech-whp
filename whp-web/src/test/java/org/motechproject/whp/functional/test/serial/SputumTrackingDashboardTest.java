package org.motechproject.whp.functional.test.serial;

import org.junit.BeforeClass;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestContainer;
import org.motechproject.whp.functional.data.TestContainers;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.ContainerDashboardPage;
import org.motechproject.whp.functional.service.ContainerDataSeed;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SputumTrackingDashboardTest extends BaseTest {

    private static TestContainers testContainers;

    @BeforeClass
    public static void setupData() {
        ContainerDataSeed containerDataSeed = new ContainerDataSeed();
        testContainers = containerDataSeed.allContainers();
    }

    @Test
    public void shouldListAllRegisteredContainersWhenNoFiltersApplied() {
        for (TestContainer testContainer : testContainers.allRegisteredContainers()) {
            ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
            assertTrue(page.hasContainerId(testContainer.getContainerId()));
            page.logout();
        }
        ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
        assertFalse(page.hasContainerId(testContainers.containerNotRegistered().getContainerId()));
        page.logout();
    }

    @Test
    public void shouldFilterByDates() {
        ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
        page.filterByContainerIssuedDate(TestContainers.containerIssuedFrom, TestContainers.containerIssuedTo);
        assertFalse(page.hasContainerId(testContainers.containerWhichDoesNotMatchDateRange().getContainerId()));
        page.logout();
    }

    @Test
    public void shouldFilterByAnAutoCompleteField() {
        ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
        page.filterByDistrict(TestContainers.district);
        assertFalse(page.hasContainerId(testContainers.containerWhichDoesNotMatchAutoCompleteField().getContainerId()));
        page.logout();
    }

    @Test
    public void shouldFilterByAllFieldsInTheFilterCriteria() {
        ContainerDashboardPage page = ContainerDashboardPage.fetch(webDriver);
        page.closeContainer(testContainers.containerWhichDoesNotMatchDropDownField().getContainerId());
        page.filterBy(TestContainers.district, TestContainers.containerStatus, TestContainers.containerIssuedFrom, TestContainers.containerIssuedTo);
        assertFalse(page.hasContainerId(testContainers.containerWhichDoesNotMatchAutoCompleteField().getContainerId()));
        assertFalse(page.hasContainerId(testContainers.containerNotRegistered().getContainerId()));
        assertFalse(page.hasContainerId(testContainers.containerWhichDoesNotMatchDateRange().getContainerId()));
        assertFalse(page.hasContainerId(testContainers.containerWhichDoesNotMatchDropDownField().getContainerId()));
        assertTrue(page.hasContainerId(testContainers.containerWhichMatchesAllCriteria().getContainerId()));
        page.logout();
    }
}


