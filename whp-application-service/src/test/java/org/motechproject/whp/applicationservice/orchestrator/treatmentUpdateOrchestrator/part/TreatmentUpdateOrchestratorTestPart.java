package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public abstract class TreatmentUpdateOrchestratorTestPart extends BaseUnitTest {

    public static final String THERAPY_ID = "therapyUid";
    public static final String PATIENT_ID = "patientid";

    @Mock
    protected WHPAdherenceService whpAdherenceService;
    @Mock
    protected PatientService patientService;
    @Mock
    protected ReportingPublisherService reportingPublisherService;

    protected Patient patient;

    protected TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    public void setUp() {
        initMocks(this);
        treatmentUpdateOrchestrator = new TreatmentUpdateOrchestrator(patientService, whpAdherenceService, reportingPublisherService);
        patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);
    }
}
