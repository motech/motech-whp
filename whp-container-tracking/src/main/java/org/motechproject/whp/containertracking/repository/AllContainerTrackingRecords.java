package org.motechproject.whp.containertracking.repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.util.IndexUploader;
import org.codehaus.jackson.type.TypeReference;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.couchdb.lucene.repository.LuceneAwareMotechBaseRepository;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.containertracking.model.ContainerTrackingRecord;
import org.motechproject.whp.containertracking.query.ContainerDashboardQueryDefinition;
import org.motechproject.whp.containertracking.query.InTreatmentContainerDashboardQueryDefinition;
import org.motechproject.whp.containertracking.query.PreTreatmentContainerDashboardQueryDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.motechproject.whp.common.domain.SputumTrackingInstance.InTreatment;
import static org.motechproject.whp.common.domain.SputumTrackingInstance.PreTreatment;

@Repository
public class AllContainerTrackingRecords extends LuceneAwareMotechBaseRepository<ContainerTrackingRecord> {

    @Autowired
    public AllContainerTrackingRecords(@Qualifier("whpContainerTrackingCouchDbConnector") LuceneAwareCouchDbConnector whpLuceneAwareCouchDbConnector) {
        super(ContainerTrackingRecord.class, whpLuceneAwareCouchDbConnector);
        IndexUploader uploader = new IndexUploader();
        ContainerDashboardQueryDefinition preTreatmentQueryDefinition = getNewQueryDefinition(PreTreatment);
        uploader.updateSearchFunctionIfNecessary(db, preTreatmentQueryDefinition.viewName(), preTreatmentQueryDefinition.searchFunctionName(), preTreatmentQueryDefinition.indexFunction());

        ContainerDashboardQueryDefinition inTreatmentQueryDefinition = getNewQueryDefinition(InTreatment);
        uploader.updateSearchFunctionIfNecessary(db, inTreatmentQueryDefinition.viewName(), inTreatmentQueryDefinition.searchFunctionName(), inTreatmentQueryDefinition.indexFunction());
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

    public TypeReference<CustomLuceneResult<ContainerTrackingRecord>> getTypeReference() {
        return new TypeReference<CustomLuceneResult<ContainerTrackingRecord>>() {
        };
    }

    public List<ContainerTrackingRecord> filter(SputumTrackingInstance instance, Properties filterParams, int skip, int limit) {
        ContainerDashboardQueryDefinition queryDefinition = getNewQueryDefinition(instance);
        if (queryDefinition != null) {
            filterParams.put(queryDefinition.getContainerInstanceFieldName(), instance.name());
            return super.filter(queryDefinition, filterParams, skip, limit);
        }
        return Collections.emptyList();
    }

    public int count(SputumTrackingInstance instance, Properties filterParams) {
        ContainerDashboardQueryDefinition queryDefinition = getNewQueryDefinition(instance);
        if (queryDefinition != null) {

            filterParams.put(queryDefinition.getContainerInstanceFieldName(), instance.name());
            return super.count(queryDefinition, filterParams);
        }
        return 0;
    }

    private ContainerDashboardQueryDefinition getNewQueryDefinition(SputumTrackingInstance instance) {
        switch (instance) {

            case PreTreatment:
                return new PreTreatmentContainerDashboardQueryDefinition();
            case InTreatment:
                return new InTreatmentContainerDashboardQueryDefinition();
        }
        return null;
    }
}