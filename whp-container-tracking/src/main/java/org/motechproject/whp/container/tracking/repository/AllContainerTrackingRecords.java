package org.motechproject.whp.container.tracking.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AllContainerTrackingRecords extends MotechBaseRepository<ContainerTrackingRecord> {

    @Autowired
    public AllContainerTrackingRecords(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(ContainerTrackingRecord.class, dbCouchDbConnector);
    }

    @View(name = "find_by_containerId", map = "function(doc) {if (doc.type ==='ContainerTrackingRecord') {emit(doc.container.containerId, doc._id);}}")
    public ContainerTrackingRecord findByContainerId(String containerId) {
        ViewQuery findByContainerId = createQuery("find_by_containerId").key(containerId).includeDocs(true);
        return singleResult(db.queryView(findByContainerId, ContainerTrackingRecord.class));
    }

    public void updateAll(List<ContainerTrackingRecord> containerTrackingRecords) {
        db.executeAllOrNothing(containerTrackingRecords);
    }

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='ContainerTrackingRecord' && doc.provider) {emit(doc.provider.providerId, doc._id);}}")
    public List<ContainerTrackingRecord> withProviderId(String providerId) {
        ViewQuery findByContainerId = createQuery("find_by_providerId").key(providerId.toLowerCase()).includeDocs(true);
        return db.queryView(findByContainerId, ContainerTrackingRecord.class);
    }

    @View(name = "find_by_patientId", map = "function(doc) {if (doc.type ==='ContainerTrackingRecord' && doc.patient) {emit(doc.patient.patientId, doc._id);}}")
    public List<ContainerTrackingRecord> withPatientId(String patientId) {
        ViewQuery findByContainerId = createQuery("find_by_patientId").key(patientId.toLowerCase()).includeDocs(true);
        return db.queryView(findByContainerId, ContainerTrackingRecord.class);
    }

    @View(name = "pre_treatment_rows", map = "function(doc) {if (doc.type ==='ContainerTrackingRecord' && doc.container.currentTrackingInstance && doc.container.currentTrackingInstance === 'PreTreatment') {emit(null, doc._id);}}")
    public List<ContainerTrackingRecord> getAllPretreatmentContainerDashboardRows(Integer skip, Integer limit) {
        ViewQuery findByContainerId = createQuery("pre_treatment_rows").skip(skip).limit(limit).includeDocs(true);
        return db.queryView(findByContainerId, ContainerTrackingRecord.class);
    }

    @View(name = "number_of_pre_treatment_rows", map = "function(doc) {if (doc.type === 'ContainerTrackingRecord' && doc.container.currentTrackingInstance && doc.container.currentTrackingInstance === 'PreTreatment') {emit(null, doc._id);}}", reduce = "_count")
    public int numberOfPreTreatmentRows() {
        ViewQuery numberOfPreTreatmentRows = createQuery("number_of_pre_treatment_rows").reduce(true);
        ViewResult viewResult = db.queryView(numberOfPreTreatmentRows);
        for (ViewResult.Row row : viewResult) {
            return row.getValueAsInt();
        }
        return 0;
    }
}