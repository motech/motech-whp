package org.motechproject.whp.adherence.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceSummaryByProviderTest {

    public static final String PROVIDER_ID = "providerId";

    public List<PatientAdherenceStatus> patientsWithAdherence;
    public List<PatientAdherenceStatus> patientsWithoutAdherence;
    public List<PatientAdherenceStatus> patients;

    @Before
    public void setUp() {
        PatientAdherenceStatus patientAdherenceStatus1 = new PatientAdherenceStatus("patient1", currentAdherenceCaptureWeek().startDate());
        PatientAdherenceStatus patientAdherenceStatus2 = new PatientAdherenceStatus("patient2", currentAdherenceCaptureWeek().startDate());

        patientsWithAdherence = asList(patientAdherenceStatus1, patientAdherenceStatus2);

        PatientAdherenceStatus patientWithoutAdherenceStatus1 = new PatientAdherenceStatus("patient3", null);
        PatientAdherenceStatus patientWithoutAdherenceStatus2 = new PatientAdherenceStatus("patient4", null);
        patientsWithoutAdherence = asList(patientWithoutAdherenceStatus1, patientWithoutAdherenceStatus2);

        patients = new ArrayList<>();
        patients.addAll(patientsWithAdherence);
        patients.addAll(patientsWithoutAdherence);
    }
    @Test
    public void shouldReturnZeroAsCountOfAllPatientsByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<PatientAdherenceStatus>());
        assertThat(summary.countOfAllPatients(), is(0));
    }

    @Test
    public void shouldCountAllPatientsUnderProvider() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, patients);
        assertThat(summary.countOfAllPatients(), is(patients.size()));
    }

    @Test
    public void shouldReturnZeroAsCountAllPatientsWithAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<PatientAdherenceStatus>());
        assertThat(summary.countOfPatientsWithAdherence(), is(0));
    }

    @Test
    public void shouldCountAllPatientsWithAdherence() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, patients);
        assertThat(summary.countOfPatientsWithAdherence(), is(patientsWithAdherence.size()));
    }

    @Test
    public void shouldReturnZeroAsCountOfAllPatientsWithoutAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<PatientAdherenceStatus>());
        assertThat(summary.countOfPatientsWithoutAdherence(), is(0));
    }

    @Test
    public void shouldReturnCountOfPatientsWithoutAdherence() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, patients);
        assertThat(summary.countOfPatientsWithoutAdherence(), is(patientsWithoutAdherence.size()));
    }

    @Test
    public void shouldReturnAllPatientsWithoutAdherence() {
        AdherenceSummaryByProvider adherenceSummaryByProvider = new AdherenceSummaryByProvider("providerId",patients);
        assertThat(adherenceSummaryByProvider.getAllPatientsWithoutAdherence(), is(patientsWithoutAdherence));
    }

    @Test
    public void shouldReturnAllPatientsWithAdherence() {
        AdherenceSummaryByProvider adherenceSummaryByProvider = new AdherenceSummaryByProvider("providerId",patients);
        assertThat(adherenceSummaryByProvider.getAllPatientsWithAdherence(), is(patientsWithAdherence));
    }


    @Test
    public void shouldReturnListOfPatientIdsWithoutAdherence() {
        AdherenceSummaryByProvider adherenceSummaryByProvider = new AdherenceSummaryByProvider("providerId", patients);
        assertEquals(asList("patient3", "patient4"), adherenceSummaryByProvider.getAllPatientIdsWithoutAdherence());
    }

}
