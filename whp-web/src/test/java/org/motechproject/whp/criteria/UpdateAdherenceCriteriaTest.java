package org.motechproject.whp.criteria;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientStatus;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.*;

public class UpdateAdherenceCriteriaTest extends BaseUnitTest {

    public static final String PATIENT_ID = "patientId";

    @Mock
    AllPatients allPatients;

    UpdateAdherenceCriteria updateAdherenceCriteria;

    @Before
    public void setup() {
        initMocks(this);
        setupPatient(PatientStatus.Open);
        updateAdherenceCriteria = new UpdateAdherenceCriteria(allPatients);
    }

    private void setupPatient(PatientStatus status) {
        reset(allPatients);
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withStatus(status).build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
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
            assertFalse(updateAdherenceCriteria.canUpdate(PATIENT_ID));
        }
    }

    @Test
    public void shouldNotBeAbleToUpdateAdherenceWhenPatientIsClosed() {
        setupPatient(PatientStatus.Closed);
        assertFalse(updateAdherenceCriteria.canUpdate(PATIENT_ID));
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
            assertTrue(updateAdherenceCriteria.canUpdate(PATIENT_ID));
        }
    }
}
