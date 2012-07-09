package org.motechproject.whp.v1.migration;

import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.v0.builder.PatientV0Builder;
import org.motechproject.whp.v0.domain.PatientV0;
import org.motechproject.whp.v0.repository.AllPatientsV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationMigrationContext.xml")
public class ToVersion0IT extends SpringIntegrationTest {

    @Autowired
    AllPatientsV0 allPatientsV0;

    @Autowired
    AllPatients allPatients;

    @Autowired
    ToVersion0 toVersion0;

    @Test
    public void shouldMigratePatient() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        allPatientsV0.add(patientV0);

        toVersion0.doo();

        assertNotNull(allPatients.findByPatientId(patientV0.getPatientId()));
    }

}
