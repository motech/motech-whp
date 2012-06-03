package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.Arrays;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class AllPatientsTest {

    @Mock
    private ViewQuery patientViewQuery;
    @Mock
    private AllTherapies allTherapies;
    @Mock
    private CouchDbConnector couchDbConnector;
    private AllPatients allPatients;

    @Before
    public void setUp() {
        initMocks(this);
        allPatients = spy(new AllPatients(couchDbConnector, allTherapies));
    }

    @Test
    public void shouldFetchPatientsWithActiveTreatment() {
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientId1").build();
        patient1.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientId2").build();

        doReturn(Arrays.asList(patient1, patient2)).when(allPatients).findByCurrentProviderId("providerId1");

        assertPatientEquals(new Patient[]{patient2}, allPatients.getAllWithActiveTreatment("providerId1").toArray());
    }
}
