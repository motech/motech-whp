package org.motechproject.whp.ivr.operation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class RecordAdherenceOperationTest {

    public static final String CURRENT_PATIENT = "patient2";
    public static final String PROVIDER = "provider";

    @Mock
    private WHPAdherenceService whpAdherenceService;

    private RecordAdherenceOperation recordAdherenceOperation;

    @Before
    public void setUp() {
        initMocks(this);
        recordAdherenceOperation = new RecordAdherenceOperation(whpAdherenceService, CURRENT_PATIENT);
    }

    @Test
    public void shouldSaveAdherenceForCurrentPatient() {
        FlowSessionStub flowSession = new FlowSessionStub();
        IvrSession ivrSession = new IvrSession(flowSession);
        ivrSession.providerId(PROVIDER);
        ivrSession.patientsWithoutAdherence(Arrays.asList("patient1", CURRENT_PATIENT));
        ivrSession.currentPatientIndex(1);

        recordAdherenceOperation.perform("2", flowSession);

        AuditParams auditParams = new AuditParams(PROVIDER, AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(CURRENT_PATIENT, currentWeekInstance(), 2);
        verify(whpAdherenceService).recordAdherence(weeklyAdherenceSummary, auditParams);
    }
}
