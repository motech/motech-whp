package org.motechproject.whp.container.tracking.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class ContainerTrackingService {

    AllProviders allProviders;
    AllPatients allPatients;
    AllReasonForContainerClosures allReasonForContainerClosures;
    AllAlternateDiagnosis allAlternateDiagnosis;

    protected abstract AllContainerTrackingRecords getRepository();

    @Autowired
    public ContainerTrackingService(AllProviders allProviders, AllPatients allPatients,
                                    AllReasonForContainerClosures allReasonForContainerClosures, AllAlternateDiagnosis allAlternateDiagnosis) {
        this.allProviders = allProviders;
        this.allPatients = allPatients;
        this.allReasonForContainerClosures = allReasonForContainerClosures;
        this.allAlternateDiagnosis = allAlternateDiagnosis;
    }

    public List<ContainerTrackingRecord> allContainerDashboardRows() {
        return getRepository().getAll();
    }

    public void createDashboardRow(Container container) {
        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);
        row.setProvider(provider(container));

        getRepository().add(row);
    }

    public void updateDashboardRow(Container container) {
        ContainerTrackingRecord trackingRecord = getRepository().findByContainerId(container.getContainerId());
        trackingRecord.setProvider(provider(container));
        trackingRecord.setPatient(patient(container));
        trackingRecord.setContainer(container);

        getRepository().update(trackingRecord);
    }

    public void updateProviderInformation(Provider provider) {
        List<ContainerTrackingRecord> allRowsBelongingToProvider = getRepository().withProviderId(provider.getProviderId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToProvider)) {
            for (ContainerTrackingRecord containerTrackingRecord : allRowsBelongingToProvider) {
                containerTrackingRecord.setProvider(provider);
            }
            getRepository().updateAll(allRowsBelongingToProvider);
        }
    }

    public void updatePatientInformation(Patient patient) {
        List<ContainerTrackingRecord> allRowsBelongingToPatient = getRepository().withPatientId(patient.getPatientId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToPatient)) {
            for (ContainerTrackingRecord containerTrackingRecord : allRowsBelongingToPatient) {
                containerTrackingRecord.setPatient(patient);
            }
            getRepository().updateAll(allRowsBelongingToPatient);
        }
    }

    private Provider provider(Container container) {
        if (isNotBlank(container.getProviderId())) {
            return allProviders.findByProviderId(container.getProviderId());
        } else {
            return null;
        }
    }

    private Patient patient(Container container) {
        if (isNotBlank(container.getPatientId())) {
            return allPatients.findByPatientId(container.getPatientId());
        } else {
            return null;
        }
    }
}
