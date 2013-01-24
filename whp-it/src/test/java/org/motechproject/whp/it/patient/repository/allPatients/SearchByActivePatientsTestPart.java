package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.user.domain.ProviderIds;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class SearchByActivePatientsTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFilterProvidersByActivePatients() {
        Patient patientWithCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId1").build();

        Patient patientWithoutCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        patientWithoutCurrentTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        allPatients.add(patientWithCurrentTreatment);
        allPatients.add(patientWithoutCurrentTreatment);

        ProviderIds providerIds = new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId(), "providerId"));

        assertEquals(new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId())), allPatients.providersWithActivePatients(providerIds));
    }

    @Test
    public void shouldFetchAllProvidersWithActivePatients() {
        Patient patientWithCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId1").build();

        Patient patientWithoutCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        patientWithoutCurrentTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        allPatients.add(patientWithCurrentTreatment);
        allPatients.add(patientWithoutCurrentTreatment);

        assertEquals(new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId())), allPatients.providersWithActivePatients());
    }

    @Test
    public void shouldReturnProviderIdsWithAdherenceAfterGivenDate() {
        Patient activeProviderOnePatient1 = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId1").withAdherenceProvidedForLastWeek().build();
        Patient activeProviderOnePatient2 = new PatientBuilder().withDefaults().withProviderId("provider2").withPatientId("patientId2").withAdherenceProvidedForLastWeek(today().minusWeeks(2)).build();
        Patient inactiveProviderOnePatient = new PatientBuilder().withDefaults().withProviderId("provider2").withPatientId("patientId3").build();


        allPatients.add(activeProviderOnePatient1);
        allPatients.add(activeProviderOnePatient2);
        allPatients.add(inactiveProviderOnePatient);


        ProviderIds providerIds = allPatients.findAllProvidersWithAdherenceAsOf(currentAdherenceCaptureWeek().startDate());

        assertThat(providerIds.asList().size(), is(1));
        assertThat(providerIds.asList(), hasItems("provider1"));
    }

    @Test
    public void shouldReturnProviderIdsWithoutAdherenceByGivenDate() {
        Patient provider1WithPatientWithAdherence = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId1").withAdherenceProvidedForLastWeek().build();
        Patient provider1WithPatientWithoutAdherenceForLastWeek = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId2").withAdherenceProvidedForLastWeek(currentAdherenceCaptureWeek().startDate().minusDays(1)).build();
        Patient provider2WithPatientWithoutAdherence = new PatientBuilder().withDefaults().withProviderId("provider2").withPatientId("patientId3").withAdherenceProvidedForLastWeek(today().minusWeeks(2)).build();
        Patient provider3WithInactivePatient = new PatientBuilder().withDefaults().withProviderId("provider2").withPatientId("patientId4").build();
        Patient provider4WithPatientWithAdherence = new PatientBuilder().withDefaults().withProviderId("provider4").withPatientId("patientId5").withAdherenceProvidedForLastWeek().build();

        allPatients.add(provider1WithPatientWithAdherence);
        allPatients.add(provider1WithPatientWithoutAdherenceForLastWeek);
        allPatients.add(provider2WithPatientWithoutAdherence);
        allPatients.add(provider3WithInactivePatient);
        allPatients.add(provider4WithPatientWithAdherence);


        ProviderIds providerIds = allPatients.findAllProvidersWithoutAdherenceAsOf(currentAdherenceCaptureWeek().startDate());

        assertThat(providerIds.asList().size(), is(2));
        assertThat(providerIds.asList(), hasItems("provider2"));
        assertThat(providerIds.asList(), hasItems("provider1"));
    }
}
