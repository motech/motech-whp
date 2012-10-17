package org.motechproject.whp.importer.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ContainerMappingImporterTest {

    @Autowired
    ContainerMappingImporter containerMappingImporter;

    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;

    @Test
    public void shouldValidateContainerMappingRequest(){
        ContainerMappingRequest requestWithNullProviderId = new ContainerMappingRequest(null, "100", "200");
        ContainerMappingRequest requestWithNonNumericContainerIdRange = new ContainerMappingRequest("1", "abcd", "200");
        ContainerMappingRequest validRequest = new ContainerMappingRequest("1", "100", "200");

        List requests = new ArrayList();
        requests.add(requestWithNullProviderId);
        assertFalse(containerMappingImporter.validate(requests).isValid());

        requests.clear();
        requests.add(requestWithNonNumericContainerIdRange);
        assertFalse(containerMappingImporter.validate(requests).isValid());

        requests.clear();
        requests.add(validRequest);
        assertTrue(containerMappingImporter.validate(requests).isValid());
    }


    @Test
    public void shouldAddContainerMappingToRepository(){
        ContainerMappingRequest containerMappingRequest1 = new ContainerMappingRequest("1", "101", "200");
        ContainerMappingRequest containerMappingRequest2 = new ContainerMappingRequest("2", "201", "300");

        List requests = new ArrayList();
        requests.add(containerMappingRequest1);
        requests.add(containerMappingRequest2);

        containerMappingImporter.post(requests);

        assertThat(allProviderContainerMappings.getAll().size(), is(2));
    }


    @Before
    @After
    public void deleteExistingContainerMappings() {
        allProviderContainerMappings.removeAll();
    }
}
