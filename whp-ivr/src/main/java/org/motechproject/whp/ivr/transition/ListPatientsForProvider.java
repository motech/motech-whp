package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.CaptureAdherenceNodeBuilder;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ListPatientsForProvider implements ITransition {

    public static String PATIENTS_WITHOUT_ADHERENCE = "patientsWithoutAdherence";

    @Autowired
    private AdherenceDataService adherenceDataService;
    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private AllProviders allProviders;

    public ListPatientsForProvider() {
    }

    public ListPatientsForProvider(AdherenceDataService adherenceDataService, WHPIVRMessage whpivrMessage, AllProviders allProviders) {
        this.adherenceDataService = adherenceDataService;
        this.whpivrMessage = whpivrMessage;
        this.allProviders = allProviders;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        String mobileNumber = flowSession.get("cid");
        String providerId = allProviders.findByPrimaryMobileNumber(mobileNumber).getProviderId();

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        List<String> patientsWithoutAdherence = adherenceSummary.getAllPatientsWithoutAdherence();


        CaptureAdherenceNodeBuilder captureAdherenceNodeBuilder = new CaptureAdherenceNodeBuilder(whpivrMessage)
                .patientSummary(adherenceSummary.countOfPatientsWithAdherence(), adherenceSummary.countOfPatientsWithoutAdherence());

        handlePatientAdherence(captureAdherenceNodeBuilder, flowSession, patientsWithoutAdherence);

        return captureAdherenceNodeBuilder.build();
    }

    private void handlePatientAdherence(CaptureAdherenceNodeBuilder captureAdherenceNodeBuilder, FlowSession flowSession, List<String> patientsWithoutAdherence) {
        if (patientsWithoutAdherence.size() > 0) {
            captureAdherenceNodeBuilder.captureAdherence(patientsWithoutAdherence.get(0),1);

            flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList<>(patientsWithoutAdherence));
        }
    }
}
