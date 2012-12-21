package org.motechproject.whp.applicationservice.adherence;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class AdherenceSubmissionServiceTest {

    private AdherenceSubmissionService adherenceSubmissionService;

    @Mock
    private ProviderService providerService;

    @Mock
    private AdherenceLogService adherenceLogService;

    @Mock
    private PatientService patientService;

    @Before
    public void setup() {
        initMocks(this);
        adherenceSubmissionService = new AdherenceSubmissionService(providerService, patientService, adherenceLogService);
    }

    @Test
    public void shouldReturnListOfAllProvidersPendingAdherence() {
        String district = "district";
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();
        ArrayList<Provider> providersPendingAdherence = new ArrayList<Provider>();

        ProviderIds providersInDistrict = new ProviderIds(asList("providerWithActivePatientWithoutAdherence", "providerWithActivePatientWithAdherence", "providerWithoutActivePatient"));
        ProviderIds providersWithActivePatients = new ProviderIds(asList("providerWithActivePatientWithoutAdherence", "providerWithActivePatientWithAdherence"));

        ProviderIds providersWhoSubmittedAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithAdherence"));
        ProviderIds providersPendingAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithoutAdherence"));

        when(providerService.findByDistrict(district)).thenReturn(providersInDistrict);
        when(patientService.providersWithActivePatients(providersInDistrict)).thenReturn(providersWithActivePatients);
        when(adherenceLogService.providersWithAdherenceRecords(providersWithActivePatients, yesterday, today)).thenReturn(providersWhoSubmittedAdherenceThisWeek);
        when(providerService.findByProviderIds(providersPendingAdherenceThisWeek)).thenReturn(providersPendingAdherence);

        assertEquals(providersPendingAdherence, adherenceSubmissionService.providersPendingAdherence(district, yesterday, today));
    }

    @Test
    public void shouldReturnListOfAllProvidersWithAdherence() {
        String district = "district";
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();
        ArrayList<Provider> providersWithAdherence = new ArrayList<Provider>();

        ProviderIds providersInDistrict = new ProviderIds(asList("providerWithActivePatientWithoutAdherence", "providerWithActivePatientWithAdherence", "providerWithoutActivePatient"));
        ProviderIds providersWithActivePatients = new ProviderIds(asList("providerWithActivePatientWithoutAdherence", "providerWithActivePatientWithAdherence"));

        ProviderIds providersWhoSubmittedAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithAdherence"));
        ProviderIds providersPendingAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithoutAdherence"));

        when(providerService.findByDistrict(district)).thenReturn(providersInDistrict);
        when(patientService.providersWithActivePatients(providersInDistrict)).thenReturn(providersWithActivePatients);
        when(adherenceLogService.providersWithAdherenceRecords(providersWithActivePatients, yesterday, today)).thenReturn(providersWhoSubmittedAdherenceThisWeek);
        when(providerService.findByProviderIds(providersPendingAdherenceThisWeek)).thenReturn(providersWithAdherence);

        assertEquals(providersWithAdherence, adherenceSubmissionService.providersWithAdherence(district, yesterday, today));
    }

    @Test
    public void shouldReturnListOfAllProvidersWithPendingAdherence() {
        LocalDate yesterday = today().minusDays(1);
        Provider providerWithPatientWithoutAdherence = new Provider("providerWithActivePatientWithoutAdherence", "msisdn", "district", now());

        LocalDate today = today();

        ProviderIds providersWhoSubmittedAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithAdherence"));
        ProviderIds providersPendingAdherenceThisWeek = new ProviderIds(asList("providerWithActivePatientWithoutAdherence"));
        List<Provider> expectedProvidersPendingAdherence =  asList(providerWithPatientWithoutAdherence);

        ProviderIds providersWithActivePatients = new ProviderIds(asList("providerWithActivePatientWithoutAdherence", "providerWithActivePatientWithAdherence"));
        when(patientService.providersWithActivePatients()).thenReturn(providersWithActivePatients);
        when(adherenceLogService.providersWithAdherenceRecords(providersWithActivePatients, yesterday, today)).thenReturn(providersWhoSubmittedAdherenceThisWeek);
        when(providerService.findByProviderIds(providersPendingAdherenceThisWeek)).thenReturn(expectedProvidersPendingAdherence);

        List<Provider> providersPendingAdherence = adherenceSubmissionService.providersPendingAdherence(yesterday, today);

        assertEquals(expectedProvidersPendingAdherence, providersPendingAdherence);
    }
}
