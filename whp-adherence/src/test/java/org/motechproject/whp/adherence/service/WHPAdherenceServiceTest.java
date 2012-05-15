package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.util.AssertAdherence;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceTest extends SpringIntegrationTest {

    public static final String PATIENT_ID = "patientId";

    LocalDate today = DateUtil.newDate(2012, 5, 3);

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

    private String user;
    private AdherenceSource source;

    @Before
    public void setup() {
        mockCurrentDate(today);
        user = "providerId";
        source = AdherenceSource.WEB;
    }

    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(PATIENT_ID)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.createPatient(patientRequest);

        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().forPatient(allPatients.findByPatientId(PATIENT_ID)).build();

        adherenceService.recordAdherence(PATIENT_ID, adherence, user, source);
        assertArrayEquals(
                adherence.getAdherenceLogs().toArray(),
                adherenceService.currentWeekAdherence(PATIENT_ID).getAdherenceLogs().toArray()
        );
    }

    @Test
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        WeeklyAdherence adherence = adherenceIsRecordedForTheFirstTime();
        assertEquals(
                adherence.firstDoseTakenOn(),
                allPatients.findByPatientId(PATIENT_ID).getCurrentProvidedTreatment().getTreatment().getDoseStartDate()
        );
    }

    @Test
    public void shouldNotStartPatientOnTreatmentWhenRecordingAdherenceForAnyWeekSubsequentToFirstEverAdherenceCapturedWeek() {
        WeeklyAdherence adherence = adherenceIsRecordedForTheFirstTime(); //capturing adherence for last week -> this is the first ever adherence captured week
        mockCurrentDate(today.plusDays(3)); //moving to the sunday -> capturing adherence for this week -> subsequent to first ever adherence captured week
        adherenceService.recordAdherence(PATIENT_ID, new WeeklyAdherence(), user, source);
        assertEquals(
                adherence.firstDoseTakenOn(),
                allPatients.findByPatientId(PATIENT_ID).getCurrentProvidedTreatment().getTreatment().getDoseStartDate()
        );
    }

    @Test
    public void shouldResetDoseStartDateWhenNoAdherenceIsPostedForSameWeek() {
        adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(1));
        adherenceService.recordAdherence(PATIENT_ID, new WeeklyAdherence(), user, source);
        assertNull(allPatients.findByPatientId(PATIENT_ID).getCurrentProvidedTreatment().getTreatment().getDoseStartDate());
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder()
                .withDefaults()
                .withCaseId(PATIENT_ID)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.createPatient(withDosesOnMonWedFri);

        WeeklyAdherence expectedAdherence = new WeeklyAdherenceBuilder()
                .withDefaultLogs()
                .forPatient(allPatients.findByPatientId(PATIENT_ID))
                .build();
        adherenceService.recordAdherence(withDosesOnMonWedFri.getCase_id(), expectedAdherence, user, source);

        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(withDosesOnMonWedFri.getCase_id());
        areSame(expectedAdherence, adherence);
    }


    @Test
    public void shouldReturnEmptyAdherenceWhenCurrentWeekAdherenceIsNotCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.createPatient(withDosesOnMonWedFri);

        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(withDosesOnMonWedFri.getCase_id());
        AssertAdherence.forWeek(adherence, Monday, Wednesday, Friday);
    }

    @After
    public void tearDown() {
        super.tearDown();
        deleteAdherenceLogs();
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

    private WeeklyAdherence adherenceIsRecordedForTheFirstTime() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(PATIENT_ID)
                .withPatientType(PatientType.New)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();

        patientService.createPatient(patientRequest);
        adherenceService.recordAdherence(PATIENT_ID, adherence, user, source);
        return adherence;
    }

    private void deleteAdherenceLogs() {
        for (Object log : allAdherenceLogs.getAll().toArray()) {
            adherenceDbConnector.delete(log);
        }
    }

}
