package org.motechproject.whp.container.it;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationContainerContext.xml")
public class AllContainersIT extends SpringIntegrationTest {
    @Autowired
    AllContainers allContainers;
    private Container container;
    private DateTime now;

    private void addAndMarkForDeletion(Container container) {
        allContainers.add(container);
        markForDeletion(container);
    }

    @Before
    public void setUp() {
        now = DateUtil.now();
        container = new Container("P00001", "1234567890", SputumTrackingInstance.PreTreatment, now);
    }

    @Test
    public void shouldSaveContainerInfo() {
        addAndMarkForDeletion(container);
        Container containerReturned = allContainers.findByContainerId("1234567890");

        assertNotNull(containerReturned);
        assertEquals("1234567890", containerReturned.getContainerId());
        assertEquals("P00001", containerReturned.getProviderId());
        assertEquals(now, containerReturned.getCreationTime());
        assertEquals("Pre-treatment", containerReturned.getInstance().getDisplayText());
    }

    @Test
    public void shouldFindContainerByPatientId() {
        addAndMarkForDeletion(container);
        String patientId = "patientid";
        container.mapWith(patientId);
        allContainers.update(container);
        Container containerReturned = allContainers.findByPatientId(patientId);

        assertNotNull(containerReturned);
        assertEquals("1234567890", containerReturned.getContainerId());
        assertEquals("P00001", containerReturned.getProviderId());
        assertEquals(patientId, containerReturned.getPatientId());
        assertEquals("Pre-treatment", containerReturned.getInstance().getDisplayText());
    }

    @Test
    public void shouldUpdateLabResults() {
        addAndMarkForDeletion(container);

        String containerId = "1234567890";
        Container containerFromDB = allContainers.findByContainerId(containerId);

        LabResults labResults = new LabResults();
        labResults.setSmearTestDate1(new LocalDate());
        labResults.setSmearTestDate2(new LocalDate());
        labResults.setSmearTestResult1(SmearTestResult.Positive);
        labResults.setSmearTestResult2(SmearTestResult.Positive);
        labResults.setLabName("labName");
        labResults.setLabNumber("labNumber");

        containerFromDB.setLabResults(labResults);

        allContainers.update(containerFromDB);
        markForDeletion(containerFromDB);

        Container containerWithLabResults = allContainers.findByContainerId(containerId);

        assertThat(containerWithLabResults.getLabResults(), is(labResults));
    }
}