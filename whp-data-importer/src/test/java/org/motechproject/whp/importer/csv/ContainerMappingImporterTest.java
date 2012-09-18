package org.motechproject.whp.importer.csv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ContainerMappingImporterTest {

    @Autowired
    ContainerMappingImporter containerMappingImporter;


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


}
