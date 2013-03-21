package org.motechproject.whp.providerreminder.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.providerreminder.domain.ScheduleType;
import org.motechproject.whp.providerreminder.ivr.ProviderAlertService;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderReminderService {

    private AdherenceSubmissionService adherenceSubmissionService;
    private ProviderAlertService alertService;

    @Autowired
    public ProviderReminderService(AdherenceSubmissionService adherenceSubmissionService, ProviderAlertService alertService) {
        this.adherenceSubmissionService = adherenceSubmissionService;
        this.alertService = alertService;
    }

    public void alertProvidersWithActivePatients(ScheduleType eventType) {
        List<Provider> providers = adherenceSubmissionService.providersToSubmitAdherence();
        if (CollectionUtils.isNotEmpty(providers)) {
            alertService.raiseIVRRequest(providers, eventType);
        }
    }

    public void alertProvidersPendingAdherence(ScheduleType eventType) {
        TreatmentWeek treatmentWeek = TreatmentWeekInstance.currentAdherenceCaptureWeek();
        List<Provider> providers = adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate());
        if (CollectionUtils.isNotEmpty(providers)) {
            alertService.raiseIVRRequest(providers, eventType);
        }
    }
}
