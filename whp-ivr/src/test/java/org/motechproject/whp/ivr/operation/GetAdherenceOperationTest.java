package org.motechproject.whp.ivr.operation;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.session.IvrSession.CURRENT_PATIENT_ADHERENCE_INPUT;

public class GetAdherenceOperationTest {
    @Mock
    FlowSession flowSession;

    GetAdherenceOperation getAdherenceOperation;
    @Before
    public void setUp() {
        initMocks(this);
        getAdherenceOperation = new GetAdherenceOperation();
    }
    @Test
    public void shouldAddAdherenceInputToSession() {
        String adherenceInput = "adherenceInput";
        getAdherenceOperation.perform(adherenceInput, flowSession);
        verify(flowSession,times(1)).set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
    }
}
