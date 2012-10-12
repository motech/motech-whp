package org.motechproject.whp.container.tracking.repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.util.IndexUploader;
import org.codehaus.jackson.type.TypeReference;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.View;
import org.motechproject.couchdb.lucene.repository.LuceneAwareMotechBaseRepository;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.query.ContainerDashboardQueryDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;


@Repository
public class AllContainerTrackingRecords extends LuceneAwareMotechBaseRepository<ContainerTrackingRecord> {

    private static final String VIEW_NAME = "ContainerTracking";
    private static final String SEARCH_FUNCTION = "findByCriteria";
    private static final String INDEX_FUNCTION = "function(doc) { " +
            "var index=new Document(); " +
            "index.add(doc.provider.providerId, {field: 'providerId'}); " +
            "index.add(doc.provider.district, {field: 'district'});" +
            "index.add(doc.container.status, {field: 'containerStatus'});" +
            "index.add(doc.container.creationTime, {field: 'containerIssuedDate', type : 'date'});" +
            "index.add(doc.container.instance, {field: 'containerInstance'}); " +

            "if(doc.container.labResults != undefined) { "+
                "index.add(doc.container.labResults.smearTestResult1, {field: 'smearTestResult1'}); "+
                "index.add(doc.container.labResults.smearTestResult2, {field: 'smearTestResult2'}); "+
            "} "+

            "if(doc.container.tbId != undefined && doc.container.tbId != null) { "+
                "index.add('Positive', {field: 'diagnosis'}); "+
            "} else {" +
                "index.add('Negative', {field: 'diagnosis'}); "+
            "}"+

            "if(doc.patient != undefined && doc.patient.currentTherapy != undefined && doc.patient.currentTherapy.currentTreatment != undefined) { "+
                "index.add(doc.patient.currentTherapy.currentTreatment.startDate, {field: 'consultationDate', type : 'date'}); " +
            "}"+

            "return index;" +
        "}";

    @Autowired
    public AllContainerTrackingRecords(@Qualifier("whpLuceneAwareCouchDbConnector") LuceneAwareCouchDbConnector whpLuceneAwareCouchDbConnector) {
        super(ContainerTrackingRecord.class, whpLuceneAwareCouchDbConnector);
        IndexUploader uploader = new IndexUploader();
        uploader.updateSearchFunctionIfNecessary(db, VIEW_NAME, SEARCH_FUNCTION, INDEX_FUNCTION);
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

    public List<ContainerTrackingRecord> filter(Properties filterParams, int skip, int limit) {
        return super.filter(new ContainerDashboardQueryDefinition(), VIEW_NAME, SEARCH_FUNCTION, filterParams, skip, limit);
    }

    public TypeReference<CustomLuceneResult<ContainerTrackingRecord>> getTypeReference() {
        return new TypeReference<CustomLuceneResult<ContainerTrackingRecord>>() {};
    }
}

