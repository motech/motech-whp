package org.motechproject.whp.adherence.criteria;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientStatus;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria.canUpdate;

public class UpdateAdherenceCriteriaTest extends BaseUnitTest {

    public static final String PATIENT_ID = "patientId";
    private Patient patient;

    @Before
    public void setup() {
        initMocks(this);
        setupPatient(PatientStatus.Open);
    }

    private void setupPatient(PatientStatus status) {
        patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withStatus(status).build();
    }

    @Test
    public void shouldNotBeAbleToUpdateAdherenceFromWednesdayToSaturday() {
        LocalDate today = DateUtil.today();
        for (LocalDate date :
                asList(
                        today.withDayOfWeek(Wednesday.getValue()),
                        today.withDayOfWeek(Thursday.getValue()),
                        today.withDayOfWeek(Friday.getValue()),
                        today.withDayOfWeek(Saturday.getValue())
                )) {
            mockCurrentDate(date);
            assertFalse(canUpdate(patient));
        }
    }

    @Test
    public void shouldNotBeAbleToUpdateAdherenceWhenPatientIsClosed() {
        setupPatient(PatientStatus.Closed);
        assertFalse(canUpdate(patient));
    }

    @Test
    public void shouldBeAbleToUpdateAdherence() {
        LocalDate today = DateUtil.today();
        for (LocalDate date :
                asList(
                        today.withDayOfWeek(Sunday.getValue()),
                        today.withDayOfWeek(Monday.getValue()),
                        today.withDayOfWeek(Tuesday.getValue())
                )) {
            mockCurrentDate(date);
            assertTrue(canUpdate(patient));
        }
    }

    @Test
    public void shouldReturnIfDateIsPartOfCurrentAdherenceCaptureWindow() {
        assertTrue(UpdateAdherenceCriteria.isWithinCurrentAdherenceWindow(today().minusDays(7)));
        assertFalse(UpdateAdherenceCriteria.isWithinCurrentAdherenceWindow(today()));
    }
}
