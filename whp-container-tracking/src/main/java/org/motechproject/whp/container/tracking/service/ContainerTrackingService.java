package org.motechproject.whp.container.tracking.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.motechproject.whp.refdata.repository.AllAlternateDiagnosisList;
import org.motechproject.whp.refdata.repository.AllReasonForContainerClosures;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
public class ContainerTrackingService {

    AllContainerTrackingRecords allContainerTrackingRecords;
    AllProviders allProviders;
    AllPatients allPatients;
    AllReasonForContainerClosures allReasonForContainerClosures;
    AllAlternateDiagnosisList allAlternateDiagnosisList;

    @Autowired
    public ContainerTrackingService(AllContainerTrackingRecords allContainerTrackingRecords, AllProviders allProviders, AllPatients allPatients,
                                    AllReasonForContainerClosures allReasonForContainerClosures, AllAlternateDiagnosisList allAlternateDiagnosisList) {
        this.allContainerTrackingRecords = allContainerTrackingRecords;
        this.allProviders = allProviders;
        this.allPatients = allPatients;
        this.allReasonForContainerClosures = allReasonForContainerClosures;
        this.allAlternateDiagnosisList = allAlternateDiagnosisList;
    }

    public List<ContainerTrackingRecord> allContainerDashboardRows() {
        return allContainerTrackingRecords.getAll();
    }

    public void createDashboardRow(Container container) {
        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);
        row.setProvider(provider(container));

        allContainerTrackingRecords.add(row);
    }

    public void updateDashboardRow(Container container) {
        ContainerTrackingRecord trackingRecord = allContainerTrackingRecords.findByContainerId(container.getContainerId());
        trackingRecord.setProvider(provider(container));
        trackingRecord.setPatient(patient(container));
        trackingRecord.setContainer(container);

        allContainerTrackingRecords.update(trackingRecord);
    }

    public void updateProviderInformation(Provider provider) {
        List<ContainerTrackingRecord> allRowsBelongingToProvider = allContainerTrackingRecords.withProviderId(provider.getProviderId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToProvider)) {
            for (ContainerTrackingRecord containerTrackingRecord : allRowsBelongingToProvider) {
                containerTrackingRecord.setProvider(provider);
            }
            allContainerTrackingRecords.updateAll(allRowsBelongingToProvider);
        }
    }

    public void updatePatientInformation(Patient patient) {
        List<ContainerTrackingRecord> allRowsBelongingToPatient = allContainerTrackingRecords.withPatientId(patient.getPatientId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToPatient)) {
            for (ContainerTrackingRecord containerTrackingRecord : allRowsBelongingToPatient) {
                containerTrackingRecord.setPatient(patient);
            }
            allContainerTrackingRecords.updateAll(allRowsBelongingToPatient);
        }
    }

    public List<ReasonForContainerClosure> getAllClosureReasons() {
        return allReasonForContainerClosures.getAll();
    }

    public List<AlternateDiagnosisList> getAllAlternateDiagnosisList() {
        return allAlternateDiagnosisList.getAll();
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
