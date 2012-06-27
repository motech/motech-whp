package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseName;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhaseUpdateOrchestratorTest {

    public static final String THERAPY_DOC_ID = "therapyDocId";

    @Mock
    private AllPatients allPatients;
    @Mock
    private WHPAdherenceService whpAdherenceService;
    @Mock
    private PatientService patientService;

    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    @Before
    public void setUp() {
        initMocks(this);
        phaseUpdateOrchestrator = new PhaseUpdateOrchestrator(allPatients, patientService, whpAdherenceService);
    }

    @Test
    public void shouldCallAdherenceServiceToFetchPillCount() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().getTherapy().setId(THERAPY_DOC_ID);
        LocalDate today = DateUtil.today();
        LocalDate startDate = today.minusDays(1);
        patient.startTherapy(startDate);
        int numberOfDosesTakenInIP = 10;

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), THERAPY_DOC_ID, startDate, today)).thenReturn(numberOfDosesTakenInIP);

        phaseUpdateOrchestrator.recomputePillCount(patient.getPatientId());

        verify(patientService).updatePillTakenCount(patient, PhaseName.IP, numberOfDosesTakenInIP);
        verify(whpAdherenceService).countOfDosesTakenBetween(patient.getPatientId(), THERAPY_DOC_ID, startDate, today);

        verifyNoMoreInteractions(whpAdherenceService);
        verifyNoMoreInteractions(patientService);
    }

}
