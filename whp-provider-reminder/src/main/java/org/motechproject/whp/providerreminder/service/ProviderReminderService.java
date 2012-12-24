package org.motechproject.whp.providerreminder.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@Service
public class ProviderReminderService {

    private AdherenceSubmissionService adherenceSubmissionService;
    private HttpClientService httpClientService;

    @Autowired
    public ProviderReminderService(AdherenceSubmissionService adherenceSubmissionService, HttpClientService httpClientService) {
        this.adherenceSubmissionService = adherenceSubmissionService;
        this.httpClientService = httpClientService;
    }

    public void alertProvidersWithActivePatients(ProviderReminderType eventType, String callBackURL, String uuid) {
        List<Provider> providers = adherenceSubmissionService.providersToSubmitAdherence();
        List<String> providerPhoneNumbers = extractPhoneNumbers(providers);

        raiseIVRRequest(callBackURL, uuid, providerPhoneNumbers, eventType);
    }

    public void alertProvidersPendingAdherence(ProviderReminderType eventType, String callBackURL, String uuid) {
        TreatmentWeek treatmentWeek = TreatmentWeekInstance.currentAdherenceCaptureWeek();
        List<Provider> providers = adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate(), treatmentWeek.endDate());
        List<String> providerPhoneNumbers = extractPhoneNumbers(providers);

        raiseIVRRequest(callBackURL, uuid, providerPhoneNumbers, eventType);
    }

    private void raiseIVRRequest(String callBackURL, String uuid, List<String> providerPhoneNumbers, ProviderReminderType event) {
        if (CollectionUtils.isNotEmpty(providerPhoneNumbers)) {
            ProviderReminderRequest providerReminderRequest = new ProviderReminderRequest(event, providerPhoneNumbers, uuid);
            httpClientService.post(callBackURL, providerReminderRequest.toXML());
        }
    }

    private List<String> extractPhoneNumbers(List<Provider> providers) {
        return extract(providers, on(Provider.class).getPrimaryMobile());
    }
}
