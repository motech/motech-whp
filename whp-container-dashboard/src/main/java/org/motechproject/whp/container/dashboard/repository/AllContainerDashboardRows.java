package org.motechproject.whp.container.dashboard.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AllContainerDashboardRows extends MotechBaseRepository<ContainerDashboardRow> {

    @Autowired
    public AllContainerDashboardRows(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(ContainerDashboardRow.class, dbCouchDbConnector);
    }

    @View(name = "find_by_containerId", map = "function(doc) {if (doc.type ==='ContainerDashboardRow') {emit(doc.container.containerId, doc._id);}}")
    public ContainerDashboardRow findByContainerId(String containerId) {
        ViewQuery findByContainerId = createQuery("find_by_containerId").key(containerId).includeDocs(true);
        return singleResult(db.queryView(findByContainerId, ContainerDashboardRow.class));
    }

    public void updateAll(List<ContainerDashboardRow> containerDashboardRows) {
        db.executeAllOrNothing(containerDashboardRows);
    }

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='ContainerDashboardRow' && doc.provider) {emit(doc.provider.providerId, doc._id);}}")
    public List<ContainerDashboardRow> withProviderId(String providerId) {
        ViewQuery findByContainerId = createQuery("find_by_providerId").key(providerId.toLowerCase()).includeDocs(true);
        return db.queryView(findByContainerId, ContainerDashboardRow.class);
    }

    @View(name = "find_by_patientId", map = "function(doc) {if (doc.type ==='ContainerDashboardRow' && doc.patient) {emit(doc.patient.patientId, doc._id);}}")
    public List<ContainerDashboardRow> withPatientId(String patientId) {
        ViewQuery findByContainerId = createQuery("find_by_patientId").key(patientId.toLowerCase()).includeDocs(true);
        return db.queryView(findByContainerId, ContainerDashboardRow.class);
    }

    @View(name = "pre_treatment_rows", map = "function(doc) {if (doc.type ==='ContainerDashboardRow' && doc.container.currentTrackingInstance && doc.container.currentTrackingInstance === 'PreTreatment') {emit(null, doc._id);}}")
    public List<ContainerDashboardRow> getAllPretreatmentContainerDashboardRows(Integer skip, Integer limit) {
        ViewQuery findByContainerId = createQuery("pre_treatment_rows").skip(skip).limit(limit).includeDocs(true);
        return db.queryView(findByContainerId, ContainerDashboardRow.class);
    }
}
