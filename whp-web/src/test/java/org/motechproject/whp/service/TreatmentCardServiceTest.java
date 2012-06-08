package org.motechproject.whp.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.contract.TreatmentCardModel;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TreatmentCardServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;

    TreatmentCardService treatmentCardService;

    private final String externalId = "externalid";

    @Before
    public void setup() {
        initMocks(this);
        treatmentCardService = new TreatmentCardService(allAdherenceLogs);
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatient_BasedOnCategory_AndIPStartDate() {
        LocalDate therapyStartDate = new LocalDate(2012, 2, 3);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate);

        AdherenceData log1 = new AdherenceData(externalId, "", new LocalDate(2012, 2, 10));
        log1.status(PillStatus.Taken.getStatus());
        AdherenceData log2 = new AdherenceData(externalId, "", new LocalDate(2012, 2, 15));
        log2.status(PillStatus.NotTaken.getStatus());
        AdherenceData log3 = new AdherenceData(externalId, "", new LocalDate(2012, 3, 12));
        log3.status(PillStatus.Unknown.getStatus());
        AdherenceData log4 = new AdherenceData(externalId, "", new LocalDate(2012, 3, 28));
        log4.status(PillStatus.Taken.getStatus());
        List<AdherenceData> adherenceData = Arrays.asList(log1, log2, log3, log4);

        when(allAdherenceLogs.findLogsInRange(externalId, therapyStartDate, therapyStartDate.plusMonths(5))).thenReturn(adherenceData);

        TreatmentCardModel treatmentCardModel = treatmentCardService.getIntensivePhaseTreatmentCardModel(patient);

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        verify(allAdherenceLogs, times(1)).findLogsInRange(externalId, therapyStartDate, therapyStartDate.plusMonths(5));
    }

    private Patient createPatientOn3DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate) {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        patient.startTherapy(therapyStartDate);
        return patient;
    }
}
