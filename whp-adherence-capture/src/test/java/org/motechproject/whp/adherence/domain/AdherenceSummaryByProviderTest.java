package org.motechproject.whp.adherence.domain;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdherenceSummaryByProviderTest {

    public static final List<String> ALL_PATIENTS = asList("patientId1", "patientId2");
    public static final String PROVIDER_ID = "providerId";
    public static final List<String> PATIENTS_WITH_ADHERENCE = asList("patientId1", "patientId2");

    @Test
    public void shouldReturnZeroAsCountOfAllPatientsByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider();
        assertThat(summary.countOfAllPatients(), is(0));
    }

    @Test
    public void shouldCountAllPatientsUnderProvider() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, ALL_PATIENTS, PATIENTS_WITH_ADHERENCE);
        assertThat(summary.countOfAllPatients(), is(2));
    }

    @Test
    public void shouldReturnZeroAsCountAllPatientsWithAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider();
        assertThat(summary.countOfPatientsWithAdherence(), is(0));
    }

    @Test
    public void shouldCountAllPatientsWithAdherence() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, ALL_PATIENTS, PATIENTS_WITH_ADHERENCE);
        assertThat(summary.countOfPatientsWithAdherence(), is(2));
    }

    @Test
    public void shouldReturnZeroAsCountOfAllPatientsWithoutAdherenceByDefault() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider();
        assertThat(summary.countOfPatientsWithoutAdherence(), is(0));
    }

    @Test
    public void shouldReturnCountOfPatientsWithoutAdherence() {
        AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(PROVIDER_ID, ALL_PATIENTS, asList("patientId1"));
        assertThat(summary.countOfPatientsWithoutAdherence(), is(1));
    }

    @Test
    public void shouldReturnAllPatientsWithoutAdherence() {
        AdherenceSummaryByProvider adherenceSummaryByProvider = new AdherenceSummaryByProvider("providerId",asList("1","2","3","4"),asList("2","3"));
        assertThat(adherenceSummaryByProvider.getAllPatientsWithoutAdherence(), is(asList("1","4")));
    }

}
