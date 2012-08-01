package org.motechproject.whp.ivr.operation;


import org.junit.Test;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.motechproject.whp.ivr.util.IvrSession.CURRENT_PATIENT_ADHERENCE_INPUT;
import static org.motechproject.whp.ivr.util.IvrSession.CURRENT_PATIENT_INDEX;

public class ResetPatientIndexOperationTest {

    @Test
    public void shouldResetPatientIndexCountInSession() {
        FlowSession flowSession = new FlowSessionStub();
        flowSession.set(CURRENT_PATIENT_INDEX, "someIndexValue");
        new ResetPatientIndexOperation().perform("someInput", flowSession);
        assertThat(flowSession.get(CURRENT_PATIENT_ADHERENCE_INPUT), is(nullValue()));
    }
}
