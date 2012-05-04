package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.AdherenceBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.util.AssertAdherence;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceTest extends SpringIntegrationTest {

    public static final String PATIENT_ID = "patientId";

    LocalDate wednesday = DateUtil.newDate(2012, 5, 3);
    TreatmentWeek week = new TreatmentWeek(wednesday).minusWeeks(1);

    @Autowired
    @Qualifier(value = "whpDbConnector")
    CouchDbConnector couchDbConnector;

    @Autowired
    @Qualifier(value = "adherenceDbConnector")
    CouchDbConnector adherenceDbConnector;

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private PatientService patientService;

    @Autowired
    private AllAdherenceLogs allAdherenceLogs;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private AllTreatments allTreatments;

    @Test
    public void shouldRecordAdherenceForPatient() {
        AdherenceLog logForMonday = new AdherenceLog(Monday, week.dateOf(Monday));
        AdherenceLog logForTuesday = new AdherenceLog(Tuesday, week.dateOf(Tuesday));

        Adherence logs = new Adherence();
        logs.setAdherenceLogs(asList(logForMonday, logForTuesday));

        adherenceService.recordAdherence(PATIENT_ID, logs);
        assertArrayEquals(
                new AdherenceLog[]{logForMonday, logForTuesday},
                adherenceService.adherenceAsOf(PATIENT_ID, wednesday).getAdherenceLogs().toArray()
        );
    }

    @Test
    public void shouldReturnEmptyAdherence_WhenAdherenceHasNeverBeenCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        patientService.add(withDosesOnMonWedFri);

        Adherence adherence = adherenceService.currentWeeksAdherence(withDosesOnMonWedFri.getCase_id());
        AssertAdherence.forWeek(adherence, Monday, Wednesday, Friday);
    }

    @Test
    public void shouldReturnCapturedAdherence() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        patientService.add(withDosesOnMonWedFri);
        Adherence expectedAdherence = new AdherenceBuilder()
                .withLog(Monday, week.dateOf(Monday), true)
                .withLog(Wednesday, week.dateOf(Wednesday), true)
                .withLog(Friday, week.dateOf(Friday), true).build();
        adherenceService.recordAdherence(withDosesOnMonWedFri.getCase_id(), expectedAdherence);

        Adherence adherence = adherenceService.currentWeeksAdherence(withDosesOnMonWedFri.getCase_id());
        areSame(expectedAdherence, adherence);
    }

    @After
    public void tearDown() {
        deleteAdherenceLogs();
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }

    private void deleteAdherenceLogs() {
        for (Object log : allAdherenceLogs.getAll().toArray()) {
            adherenceDbConnector.delete(log);
        }
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

}
