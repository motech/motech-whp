package org.motechproject.whp.applicationservice.adherence;

import org.motechproject.whp.common.domain.ProviderPatientCount;
import org.motechproject.whp.user.domain.ProviderIds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProviderAdherenceStatus {

    private Map<String, ProviderAdherenceStatus> providerAdherenceStatusMap = new HashMap<>();

    public AllProviderAdherenceStatus(List<ProviderPatientCount> allProviderPatientCounts, List<ProviderPatientCount> providerPatientWithAdherenceCounts) {
        for(ProviderPatientCount providerPatientCount : allProviderPatientCounts){
            add(new ProviderAdherenceStatus(providerPatientCount.getProviderId(), providerPatientCount.getPatientCount()));
        }

        for(ProviderPatientCount providerPatientWithAdherenceCount : providerPatientWithAdherenceCounts){
            updateAdherenceStatus(providerPatientWithAdherenceCount);
        }
    }

    private void updateAdherenceStatus(ProviderPatientCount providerPatientWithAdherenceCount) {
        ProviderAdherenceStatus adherenceStatus = getByProviderId(providerPatientWithAdherenceCount.getProviderId());
        if(adherenceStatus != null){
            adherenceStatus.setPatientWithAdherenceCount(providerPatientWithAdherenceCount.getPatientCount());
        }
    }

    private void add(ProviderAdherenceStatus providerAdherenceStatus) {
        providerAdherenceStatusMap.put(providerAdherenceStatus.getProviderId(), providerAdherenceStatus);
    }

    private ProviderAdherenceStatus getByProviderId(String providerId) {
        return providerAdherenceStatusMap.get(providerId);
    }

    public ProviderIds providersWithPendingAdherence() {
        ProviderIds providerIds = new ProviderIds();
        for(ProviderAdherenceStatus providerAdherenceStatus : providerAdherenceStatusMap.values()){
            if(providerAdherenceStatus.isAdherencePending()){
                providerIds.add(providerAdherenceStatus.getProviderId());
            }
        }
        return providerIds;
    }
}
