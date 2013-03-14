package org.motechproject.whp.it.container;

import junit.framework.Assert;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ContainerId;
import org.motechproject.whp.container.domain.ContainerRegistrationDetails;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.repository.AllContainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllContainersIT extends SpringIntegrationTest {
    @Autowired
    AllContainers allContainers;
    private Container container;
    private DateTime now;
    String containerIdNumber = "12345";
    String providerId = "P00001";
    private ContainerId containerId;

    private void addAndMarkForDeletion(Container container) {
        allContainers.add(container);
        markForDeletion(container);
    }

    @Before
    public void setUp() {
        now = DateUtil.now();
        containerId = new ContainerId(providerId, containerIdNumber, ON_BEHALF_OF_PROVIDER);
        container = new Container(providerId, containerId, RegistrationInstance.PreTreatment, now, "d1");
        container.setContainerRegistrationDetails(new ContainerRegistrationDetails("name", "id", 99, Gender.O));
    }

    @Test
    public void shouldSaveContainerInfo() {
        addAndMarkForDeletion(container);
        Container containerReturned = allContainers.findByContainerId(containerId.value());

        assertNotNull(containerReturned);
        Assert.assertEquals(containerId.value(), containerReturned.getContainerId());
        Assert.assertEquals("p00001", containerReturned.getProviderId());
        Assert.assertEquals(now.toString(WHPDate.DATE_TIME_FORMAT), containerReturned.getCreationTime().toString(WHPDate.DATE_TIME_FORMAT));
        Assert.assertEquals("Pre-treatment", containerReturned.getInstance().getDisplayText());
        Assert.assertEquals("d1", containerReturned.getDistrict());

        assertContainerDetails(container, containerReturned);
    }

    private void assertContainerDetails(Container expected, Container actual) {
        ContainerRegistrationDetails expectedDetails = expected.getContainerRegistrationDetails();
        ContainerRegistrationDetails actualDetails = actual.getContainerRegistrationDetails();

        assertEquals(expectedDetails.getPatientId(), actualDetails.getPatientId());
        assertEquals(expectedDetails.getPatientName(), actualDetails.getPatientName());
        assertEquals(expectedDetails.getPatientAge(), actualDetails.getPatientAge());
        assertEquals(expectedDetails.getPatientGender(), actualDetails.getPatientGender());
    }

    @Test
    public void shouldUpdateLabResults() {
        addAndMarkForDeletion(container);

        Container containerFromDB = allContainers.findByContainerId(containerId.value());

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

        Container containerWithLabResults = allContainers.findByContainerId(containerId.value());

        MatcherAssert.assertThat(containerWithLabResults.getLabResults(), Is.is(labResults));
    }
}