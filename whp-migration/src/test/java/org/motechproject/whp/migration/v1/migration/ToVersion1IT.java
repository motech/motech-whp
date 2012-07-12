package org.motechproject.whp.migration.v1.migration;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.migration.v0.builder.PatientV0Builder;
import org.motechproject.whp.migration.v0.builder.TherapyV0Builder;
import org.motechproject.whp.migration.v0.domain.PatientV0;
import org.motechproject.whp.migration.v0.domain.TherapyV0;
import org.motechproject.whp.migration.v0.repository.AllPatientsV0;
import org.motechproject.whp.migration.v0.repository.AllTherapiesV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationMigrationContext.xml")
public class ToVersion1IT extends SpringIntegrationTest {

    @Autowired
    AllPatientsV0 allPatientsV0;

    @Autowired
    AllTherapiesV0 allTherapiesV0;

    @Autowired
    AllPatients allPatients;

    @Autowired
    ToVersion1 toVersion0;

    @Test
    public void shouldMigratePatient() {
        TherapyV0 therapyV0 = new TherapyV0Builder().withDefaults().withTherapyDocId(null).build();
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(therapyV0).build();
        allTherapiesV0.add(patientV0.latestTherapy());
        patientV0.getCurrentTreatment().setTherapyDocId(patientV0.latestTherapy().getId());
        allPatientsV0.add(patientV0);

        toVersion0.doo();

        assertNotNull(allPatients.findByPatientId(patientV0.getPatientId()));
    }

    @Test
    public void should(){
        toVersion0.doo();
    }

    @After
    public void tearDown() {
        markForDeletion(allPatientsV0.getAllVersionedDocs().toArray());
        markForDeletion(allTherapiesV0.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
        super.after();
    }

}
