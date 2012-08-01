package org.motechproject.whp.ivr.operation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.service.PatientService;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class RecordAdherenceOperationTest {

    public static final String CURRENT_PATIENT = "patient2";
    public static final String PROVIDER = "provider";

    @Mock
    private WHPAdherenceService whpAdherenceService;
    @Mock
    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    @Mock
    private PatientService patientService;

    private RecordAdherenceOperation recordAdherenceOperation;
    private int adherenceInput;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceInput = 3;
        recordAdherenceOperation = new RecordAdherenceOperation(adherenceInput, CURRENT_PATIENT, whpAdherenceService, phaseUpdateOrchestrator, patientService);
    }

    @Test
    public void shouldSaveAdherenceForCurrentPatient() {
        FlowSessionStub flowSession = new FlowSessionStub();
        IvrSession ivrSession = new IvrSession(flowSession);
        ivrSession.providerId(PROVIDER);
        ivrSession.patientsWithoutAdherence(Arrays.asList("patient1", CURRENT_PATIENT));
        ivrSession.currentPatientIndex(1);

        recordAdherenceOperation.perform("whatever", flowSession);

        AuditParams auditParams = new AuditParams(PROVIDER, AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(CURRENT_PATIENT, currentWeekInstance(), adherenceInput);
        verify(whpAdherenceService).recordAdherence(weeklyAdherenceSummary, auditParams);
    }
}
