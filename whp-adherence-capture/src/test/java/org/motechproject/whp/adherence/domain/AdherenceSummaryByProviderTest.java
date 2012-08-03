package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.sum;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdherenceSummaryByProviderTest {

    public static final String PROVIDER_ID = "providerId";

    public List<Patient> patientsWithAdherence;
    public List<Patient> patientsWithoutAdherence;
    public List<Patient> patients;

    @Before
    public void setUp() {
        LocalDate lastWeekStartDate = TreatmentWeekInstance.currentWeekInstance().startDate();
        Patient patientWithAdherence1 = new PatientBuilder().withPatientId("patient1").withLastAdherenceProvidedWeekStartDate(lastWeekStartDate).build();
        Patient patientWithAdherence2 = new PatientBuilder().withPatientId("patient2").withLastAdherenceProvidedWeekStartDate(lastWeekStartDate).build();

        patientsWithAdherence = asList(patientWithAdherence1, patientWithAdherence2);

        Patient patientWithoutAdherence1 = new PatientBuilder().withPatientId("patient3").build();
        Patient patientWithoutAdherence2 = new PatientBuilder().withPatientId("patient4").build();
        patientsWithoutAdherence = asList(patientWithoutAdherence1, patientWithoutAdherence2);

        patients = new ArrayList<>();
        patients.addAll(patientsWithAdherence);
        patients.addAll(patientsWithoutAdherence);
    }
    @Test
    public void shouldReturnZeroAsCountOfAllPatientsByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<Patient>());
        assertThat(summary.countOfAllPatients(), is(0));
    }

    @Test
    public void shouldCountAllPatientsUnderProvider() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, patients);
        assertThat(summary.countOfAllPatients(), is(patients.size()));
    }

    @Test
    public void shouldReturnZeroAsCountAllPatientsWithAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<Patient>());
        assertThat(summary.countOfPatientsWithAdherence(), is(0));
    }

    @Test
    public void shouldCountAllPatientsWithAdherence() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, patients);
        assertThat(summary.countOfPatientsWithAdherence(), is(patientsWithAdherence.size()));
    }

    @Test
    public void shouldReturnZeroAsCountOfAllPatientsWithoutAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, new ArrayList<Patient>());
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

}
