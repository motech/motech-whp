package org.motechproject.whp.ivr.operation;


import org.junit.Test;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.motechproject.whp.ivr.session.IvrSession.CURRENT_PATIENT_ADHERENCE_INPUT;
import static org.motechproject.whp.ivr.session.IvrSession.CURRENT_PATIENT_INDEX;
import static org.motechproject.whp.ivr.session.IvrSession.PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION;

public class ResetFlowSessionOperationTest {

    @Test
    public void shouldResetPatientIndexCountInSession() {
        FlowSession flowSession = new FlowSessionStub();
        flowSession.set(CURRENT_PATIENT_INDEX, "someIndexValue");
        new ResetFlowSessionOperation().perform("someInput", flowSession);
        assertThat(flowSession.get(CURRENT_PATIENT_ADHERENCE_INPUT), is(nullValue()));
    }

    @Test
    public void shouldResetPatientWithAdherenceInSession() {
        FlowSession flowSession = new FlowSessionStub();
        SerializableList<String> patientsWithAdherenceRecordedInSession = new SerializableList<>(new ArrayList<String>());
        patientsWithAdherenceRecordedInSession.add("patient1");
        flowSession.set(PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION, patientsWithAdherenceRecordedInSession);

        new ResetFlowSessionOperation().perform("someInput", flowSession);
        assertThat(flowSession.get(PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION), is(nullValue()));
    }


}
