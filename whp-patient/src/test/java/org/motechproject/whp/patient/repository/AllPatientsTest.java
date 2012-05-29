package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class AllPatientsTest {

    @Mock
    private ViewQuery patientViewQuery;
    @Mock
    private AllTreatments allTreatments;
    @Mock
    private CouchDbConnector couchDbConnector;
    private AllPatients allPatients;

    @Before
    public void setUp() {
        initMocks(this);
        allPatients = spy(new AllPatients(couchDbConnector, allTreatments));
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
