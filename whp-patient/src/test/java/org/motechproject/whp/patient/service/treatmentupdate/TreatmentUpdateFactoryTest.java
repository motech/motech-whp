package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Test;
import org.motechproject.whp.patient.command.OpenNewTreatment;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.TreatmentUpdateScenario;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class TreatmentUpdateFactoryTest extends SpringIntegrationTest {

    @Autowired
    private UpdateCommandFactory treatmentUpdateFactory;
    @Autowired
    private OpenNewTreatment openNewTreatment;

    @Test
    public void shouldReturnTreatmentUpdate() {
        assertEquals(openNewTreatment, treatmentUpdateFactory.updateFor(TreatmentUpdateScenario.New.getScope()));
    }

    @Test
    public void shouldNotReturnNonRelevantTreatmentUpdate() {
        assertFalse(treatmentUpdateFactory.updateFor(TreatmentUpdateScenario.Pause.getScope()).equals(openNewTreatment));
    }
}
