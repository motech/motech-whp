package org.motechproject.whp.container.tracking.service;

import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.container.tracking.repository.AllPreTreatmentContainerTrackingRecordsImpl;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreTreatmentContainerTrackingServiceImpl extends ContainerTrackingService {
    private AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords;

    @Autowired
    public PreTreatmentContainerTrackingServiceImpl(AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords, AllProviders allProviders, AllPatients allPatients, AllReasonForContainerClosures allReasonForContainerClosures, AllAlternateDiagnosis allAlternateDiagnosis) {
        super(allProviders, allPatients, allReasonForContainerClosures, allAlternateDiagnosis);
        this.allPreTreatmentContainerTrackingRecords = allPreTreatmentContainerTrackingRecords;
    }

    @Override
    protected AllContainerTrackingRecords getRepository() {
        return allPreTreatmentContainerTrackingRecords;
    }
}
