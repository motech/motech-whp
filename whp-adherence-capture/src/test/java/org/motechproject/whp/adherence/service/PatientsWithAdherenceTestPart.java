package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PatientsWithAdherenceTestPart.GivenADateRange.class,
        PatientsWithAdherenceTestPart.GivenPatientsWithProvidedAdherence.class
})
public class PatientsWithAdherenceTestPart extends WHPAdherenceServiceTestPart {

    public static class GivenADateRange extends WHPAdherenceServiceTestPart {

        @Test
        public void shouldFindAllPatientsWithProvidedAdherence() {
            String providerId = "providerId1";

            Patient patient1 = new PatientBuilder().withDefaults().withProviderId(providerId).withPatientId("patient1").build();
            Patient patient2 = new PatientBuilder().withDefaults().withProviderId(providerId).withPatientId("patient2").build();

            WeeklyAdherenceSummary summaryForPatient1 = new WeeklyAdherenceSummaryBuilder().forPatient(patient1).forWeek(new LocalDate(2012, 7, 16)).withDosesTaken(3).build();
            WeeklyAdherenceSummary summaryForPatient2 = new WeeklyAdherenceSummaryBuilder().forPatient(patient2).forWeek(new LocalDate(2012, 7, 18)).withDosesTaken(0).build();

            allPatients.add(patient1);
            allPatients.add(patient2);

            adherenceService.recordWeeklyAdherence(summaryForPatient1, patient1, auditParams);
            adherenceService.recordWeeklyAdherence(summaryForPatient2, patient2, auditParams);


            assertArrayEquals(
                    new String[]{"patient1", "patient2"},
                    adherenceService.patientsWithAdherence(providerId, summaryForPatient1.getWeek()).toArray()
            );
        }

        @Test
        public void shouldNotFindPatientsWithNoProvidedAdherence() {
            String providerId = "providerId1";

            Patient patient1 = new PatientBuilder().withDefaults().withProviderId(providerId).withPatientId("patient1").build();
            allPatients.add(patient1);

            assertTrue(adherenceService.patientsWithAdherence(providerId, new TreatmentWeek(today)).isEmpty());
        }
    }

    public static class GivenPatientsWithProvidedAdherence extends WHPAdherenceServiceTestPart {

        @Test
        public void shouldFindPatientsForProvider() {
            String providerId = "providerId1";

            Patient patient1 = new PatientBuilder().withDefaults().withProviderId(providerId).withPatientId("patient1").build();

            WeeklyAdherenceSummary summaryForPatient1 = new WeeklyAdherenceSummaryBuilder().forPatient(patient1).forWeek(new LocalDate(2012, 7, 16)).withDosesTaken(3).build();

            allPatients.add(patient1);

            adherenceService.recordWeeklyAdherence(summaryForPatient1, patient1, auditParams);

            assertTrue(adherenceService.patientsWithAdherence("providerId2", summaryForPatient1.getWeek()).isEmpty());
        }
    }
}
