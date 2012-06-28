package org.motechproject.whp.treatmentcard.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherence.builder.AdherenceDataBuilder.createLog;

public class TreatmentCardServiceTest {

    @Mock
    WHPAdherenceService whpAdherenceService;
    @Mock
    AllPatients allPatients;

    TreatmentCardService treatmentCardService;

    @Before
    public void setup() {
        initMocks(this);
        treatmentCardService = new TreatmentCardService(allPatients, whpAdherenceService);
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatient_BasedOnCategory_AndIPStartDate() {
        LocalDate therapyStartDate = new LocalDate(2011, 2, 3);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(therapyStartDate, "1");

        String therapyDocId = patient.currentTherapy().getId();
        Adherence log1 = createLog(new LocalDate(2011, 2, 10), therapyDocId, PillStatus.Taken);
        Adherence log2 = createLog(new LocalDate(2011, 2, 15), therapyDocId, PillStatus.NotTaken);
        Adherence log3 = createLog(new LocalDate(2011, 3, 12), therapyDocId, PillStatus.Unknown);
        Adherence log4 = createLog(new LocalDate(2011, 3, 28), therapyDocId, PillStatus.Taken);
        AdherenceList adherenceData = new AdherenceList(asList(log1, log2, log3, log4));

        when(whpAdherenceService.findLogsInRange(patient.getPatientId(), therapyDocId, therapyStartDate, today())).thenReturn(adherenceData);

        TreatmentCard treatmentCard = treatmentCardService.treatmentCard(patient);

        assertEquals(6, treatmentCard.getMonthlyAdherences().size());
        verify(whpAdherenceService, times(1)).findLogsInRange(patient.getPatientId(), therapyDocId, therapyStartDate, today());
    }

    @Test
    public void shouldBuildIPTreatmentCardTillTodayIfEndDateIsInFuture() {
        LocalDate today = today();
        LocalDate therapyStartDate = today.minusMonths(1);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(therapyStartDate, "1");

        String therapyDocId = patient.currentTherapy().getId();
        Adherence log1 = createLog(today.minusDays(10), therapyDocId, PillStatus.Taken);
        AdherenceList adherenceData = new AdherenceList(asList(log1));

        when(whpAdherenceService.findLogsInRange(patient.getPatientId(), therapyDocId, therapyStartDate, today)).thenReturn(adherenceData);

        treatmentCardService.treatmentCard(patient);

        verify(whpAdherenceService, times(1)).findLogsInRange(patient.getPatientId(), therapyDocId, therapyStartDate, today);
    }

    private Patient createPatientOn3DayAWeekTreatmentCategory(LocalDate therapyStartDate, String therapyDocId) {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(therapyStartDate);
        patient.currentTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setTherapyDocId("1");
        //patient.getCurrentTreatment().getTherapy().setId("1");
        patient.getCurrentTreatment().setStartDate(therapyStartDate);
        return patient;
    }

}
