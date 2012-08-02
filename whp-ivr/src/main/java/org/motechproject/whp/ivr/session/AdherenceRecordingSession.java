package org.motechproject.whp.ivr.session;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.util.IvrSession;
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

    public IvrSession initialize(FlowSession session) {
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

    public IvrSession collectingAdherenceInput(String forPatient, FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        ivrSession.startOfAdherenceSubmission(DateUtil.now());
        return ivrSession;
    }
}
