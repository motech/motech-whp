package org.motechproject.whp.v0.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.v0.builder.PatientV0Builder;
import org.motechproject.whp.v0.domain.PatientV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationMigrationContext.xml")
public class AllPatientsV0IT extends SpringIntegrationTest {

    @Autowired
    private AllPatientsV0 allPatientsV0;

    @Before
    public void setUp() {
    }

    @Test
    public void allViewShouldDefaultToAllWithVersionView() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        allPatientsV0.add(patientV0);

        List<PatientV0> all = allPatientsV0.getAllVersionedDocs();
        assertEquals(1, all.size());
        assertEquals(patientV0.getId(), all.get(0).getId());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatientsV0.getAll());
    }

}



