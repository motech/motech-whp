package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
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
import static org.junit.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;
import static org.motechproject.whp.patient.domain.PatientType.New;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceTest extends SpringIntegrationTest {

    public static final String PATIENT_ID = "patientId";

    LocalDate today = DateUtil.newDate(2012, 5, 3);
    TreatmentWeek treatmentWeek = new TreatmentWeek(today).minusWeeks(1);

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

    @Before
    public void setup(){
        mockCurrentDate(today);
    }

    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withCaseId(PATIENT_ID)
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .build();
        patientService.add(patientRequest);

        AdherenceLog logForMonday = new AdherenceLog(Monday, treatmentWeek.dateOf(Monday));
        AdherenceLog logForTuesday = new AdherenceLog(Tuesday, treatmentWeek.dateOf(Tuesday));

        Adherence logs = new Adherence();
        logs.setAdherenceLogs(asList(logForMonday, logForTuesday));

        adherenceService.recordAdherence(PATIENT_ID, logs);
        assertArrayEquals(
                new AdherenceLog[]{logForMonday, logForTuesday},
                adherenceService.adherenceAsOf(PATIENT_ID, today).getAdherenceLogs().toArray()
        );
    }

    @Test
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withCaseId(PATIENT_ID)
                                                                   .withPatientType(New)
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .build();
        patientService.add(patientRequest);

        adherenceService.recordAdherence(PATIENT_ID, new Adherence());
        assertEquals(
                today,
                allPatients.findByPatientId(PATIENT_ID).getCurrentProvidedTreatment().getTreatment().getDoseStartDate()
        );
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults()
                                                                         .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                         .build();
        patientService.add(withDosesOnMonWedFri);

        Adherence expectedAdherence = new AdherenceBuilder()
                .withLog(Monday, treatmentWeek.dateOf(Monday), true)
                .withLog(Wednesday, treatmentWeek.dateOf(Wednesday), true)
                .withLog(Friday, treatmentWeek.dateOf(Friday), true).build();
        adherenceService.recordAdherence(withDosesOnMonWedFri.getCase_id(), expectedAdherence);

        Adherence adherence = adherenceService.currentWeekAdherence(withDosesOnMonWedFri.getCase_id());
        areSame(expectedAdherence, adherence);
    }

    @Test
    public void shouldReturnEmptyAdherenceWhenCurrentWeekAdherenceIsNotCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults()
                                                                         .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                         .build();
        patientService.add(withDosesOnMonWedFri);

        Adherence adherence = adherenceService.currentWeekAdherence(withDosesOnMonWedFri.getCase_id());
        AssertAdherence.forWeek(adherence, Monday, Wednesday, Friday);
    }

    @After
    public void tearDown() {
        super.tearDown();
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
