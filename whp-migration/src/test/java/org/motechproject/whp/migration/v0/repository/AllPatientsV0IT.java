package org.motechproject.whp.migration.v0.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.migration.v0.builder.PatientV0Builder;
import org.motechproject.whp.migration.v0.builder.TherapyV0Builder;
import org.motechproject.whp.migration.v0.domain.PatientV0;
import org.motechproject.whp.migration.v0.domain.TherapyV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationMigrationContext.xml")
public class AllPatientsV0IT extends SpringIntegrationTest {

    @Autowired
    private AllPatientsV0 allPatientsV0;


    @Autowired
    private AllTherapiesV0 allTherapiesV0;

    @Before
    public void setUp() {
    }

    @Test
    public void allViewShouldDefaultToAllWithVersionView() {
        TherapyV0 therapyV0 = new TherapyV0Builder().withDefaults().withTherapyDocId(null).build();
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(therapyV0).build();
        allTherapiesV0.add(patientV0.latestTherapy());
        patientV0.getCurrentTreatment().setTherapyDocId(patientV0.latestTherapy().getId());
        allPatientsV0.add(patientV0);

        List<PatientV0> all = allPatientsV0.getAllVersionedDocs();
        assertEquals(1, all.size());
        assertEquals(patientV0.getId(), all.get(0).getId());
        assertNotNull(patientV0.getCurrentTreatment().getTherapy());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatientsV0.getAll());
        markForDeletion(allTherapiesV0.getAll());
    }

}



