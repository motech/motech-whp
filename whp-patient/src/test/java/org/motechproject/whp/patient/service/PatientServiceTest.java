package org.motechproject.whp.patient.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.user.seed.ProviderSeed;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientServiceTest {
    PatientService patientService;
    @Mock
    AllPatients allPatients;

    @Mock
    AllTherapyRemarks allTherapyRemarks;
    @Mock
    UpdateCommandFactory updateCommandFactory;
    @Mock
    RequestValidator requestValidator;
    @Mock
    ProviderService providerService;
    @Before
    public void setUp()
    {
        initMocks(this);
        patientService = new PatientService(allPatients, allTherapyRemarks, updateCommandFactory, requestValidator, providerService);
    }
    @Test
    public void shouldFetchAllRemarksUnderCurrentTherapy(){
        String therapyId = "therapyId";
        Patient patient = new Patient();
        Therapy currentTherapy = new Therapy();
        currentTherapy.setUid(therapyId);
        patient.setCurrentTherapy(currentTherapy);
        List<TherapyRemark> therapyRemarks = mock(List.class);

        when(allTherapyRemarks.findByTherapyId(therapyId)).thenReturn(therapyRemarks);

        assertThat(patientService.getRemarks(patient), is(therapyRemarks));
    }
}
