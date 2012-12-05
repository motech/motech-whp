package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategoryType;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

public class AdherenceServiceTest {

    public static final String GOVT_CATEGORY_VALID_RANGE_TO = "3";
    public static final String GOVT_CATEGORY_VALID_RANGE_FROM = "0";
    private AdherenceService adherenceService;

    @Mock
    private WHPAdherenceService whpAdherenceService;

    @Mock
    private PatientService patientService;

    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(whpAdherenceService, patientService);
    }

    @Test
    public void shouldSummarizeAdherenceSubmittedByProvider() {
        String providerId = "providerid";
        LocalDate today = today();
        List<String> patientsWithAdherence = asList("1234", "5678");
        List<Patient> patientsForProvider = asList(patients("1234"), patients("5678"));

        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(patientsForProvider);
        when(whpAdherenceService.patientsWithAdherence(providerId, week(today))).thenReturn(patientsWithAdherence);

        assertEquals(
                new AdherenceCaptureFlashingResponse(patientsWithAdherence, asList("1234", "5678")),
                adherenceService.adherenceSummary(providerId, today)
        );
    }

    @Test
    public void shouldReturnSuccessfulResponseForValidDoseCount() {
        String patientId = "1234";
        String validDoseCount = GOVT_CATEGORY_VALID_RANGE_TO;

        Patient govtCategoryPatient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        AdherenceValidationResponse actualValidationResponse = adherenceService.validateDosage(patientId, validDoseCount);

        assertEquals(new AdherenceValidationResponse("success"), actualValidationResponse);
        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnFailureResponseForInValidDoseCount() {
        String patientId = "1234";
        String inValidDoseCount = "7";

        Patient govtCategoryPatient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        AdherenceValidationResponse actualValidationResponse = adherenceService.validateDosage(patientId, inValidDoseCount);

        AdherenceValidationResponse failureResponse = new AdherenceValidationResponse("failure");
        failureResponse.setTreatmentCategory(TreatmentCategoryType.GOVERNMENT.name());
        failureResponse.setValidRangeFrom(GOVT_CATEGORY_VALID_RANGE_FROM);
        failureResponse.setValidRangeTo(GOVT_CATEGORY_VALID_RANGE_TO);

        assertEquals(failureResponse, actualValidationResponse);
        verify(patientService).findByPatientId(patientId);
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
