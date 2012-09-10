package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.command.OpenNewTreatment;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class UpdateCommandFactoryIT extends SpringIntegrationTest {

    @Autowired
    private UpdateCommandFactory treatmentUpdateFactory;
    @Autowired
    private OpenNewTreatment openNewTreatment;

    @Test
    public void shouldReturnTreatmentUpdate() {
        assertEquals(openNewTreatment, treatmentUpdateFactory.updateFor(UpdateScope.openTreatment));
    }

    @Test
    public void shouldNotReturnNonRelevantTreatmentUpdate() {
        assertFalse(treatmentUpdateFactory.updateFor(UpdateScope.pauseTreatment).equals(openNewTreatment));
    }

}
