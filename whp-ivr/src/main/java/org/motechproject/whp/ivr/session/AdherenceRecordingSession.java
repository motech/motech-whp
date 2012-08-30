package org.motechproject.whp.ivr.session;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceRecordingSession {

    private AllProviders allProviders;
    private AdherenceDataService dataService;

    @Autowired
    public AdherenceRecordingSession(AllProviders allProviders, AdherenceDataService dataService) {
        this.allProviders = allProviders;
        this.dataService = dataService;
    }

    public FlowSession initialize(FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        if (ivrSession.providerId() == null) {
            String providerId = allProviders.findByMobileNumber(ivrSession.mobileNumber()).getProviderId();
            AdherenceSummaryByProvider adherenceSummary = dataService.getAdherenceSummary(providerId);

            ivrSession.providerId(providerId);
            ivrSession.patientsWithoutAdherence(adherenceSummary.getAllPatientsWithoutAdherence());
            ivrSession.patientsWithAdherence(adherenceSummary.getAllPatientsWithAdherence());
        }
        return session;
    }
}
