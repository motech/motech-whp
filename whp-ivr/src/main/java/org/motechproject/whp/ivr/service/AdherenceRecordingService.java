package org.motechproject.whp.ivr.service;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceRecordingService {

    private AllProviders allProviders;
    private AdherenceDataService dataService;

    @Autowired
    public AdherenceRecordingService(AllProviders allProviders, AdherenceDataService dataService) {
        this.allProviders = allProviders;
        this.dataService = dataService;
    }

    public IvrSession prepareSession(FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        if (ivrSession.providerId() == null) {
            String providerId = allProviders.findByMobileNumber(ivrSession.getMobileNumber()).getProviderId();
            AdherenceSummaryByProvider adherenceSummary = dataService.getAdherenceSummary(providerId);

            ivrSession.providerId(providerId);
            ivrSession.patientsWithoutAdherence(adherenceSummary.getAllPatientsWithoutAdherence());
            ivrSession.patientsWithAdherence(adherenceSummary.getAllPatientsWithAdherence());
        }
        return ivrSession;
    }

    public IvrSession recordAdherence(IvrSession ivrSession) {
        return ivrSession;
    }

    public IvrSession skipAdherence(IvrSession ivrSession) {
        return ivrSession;
    }
}
