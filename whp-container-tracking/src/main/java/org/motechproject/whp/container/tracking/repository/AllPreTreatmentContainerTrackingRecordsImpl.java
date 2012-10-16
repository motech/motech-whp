package org.motechproject.whp.container.tracking.repository;

import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.util.IndexUploader;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.query.ContainerDashboardQueryDefinition;
import org.motechproject.whp.container.tracking.query.PreTreatmentContainerDashboardQueryDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;

@Repository
public class AllPreTreatmentContainerTrackingRecordsImpl extends AllContainerTrackingRecords {

    @Autowired
    public AllPreTreatmentContainerTrackingRecordsImpl(@Qualifier("whpContainerTrackingCouchDbConnector") LuceneAwareCouchDbConnector whpLuceneAwareCouchDbConnector) {
        super(whpLuceneAwareCouchDbConnector);
        IndexUploader uploader = new IndexUploader();
        ContainerDashboardQueryDefinition queryDefinition = getNewQueryDefinition();
        uploader.updateSearchFunctionIfNecessary(db, queryDefinition.viewName(), queryDefinition.searchFunctionName(), queryDefinition.indexFunction());
    }

    protected ContainerDashboardQueryDefinition getNewQueryDefinition() {
        return new PreTreatmentContainerDashboardQueryDefinition();
    }

    public List<ContainerTrackingRecord> filterPreTreatmentRecords(Properties filterParams, int skip, int limit) {
        ContainerDashboardQueryDefinition queryDefinition = getNewQueryDefinition();
        filterParams.put(queryDefinition.getContainerInstanceFieldName(), SputumTrackingInstance.PreTreatment.name());
        return super.filter(queryDefinition, filterParams, skip, limit);
    }

    public int countPreTreatmentRecords(Properties filterParams) {
        ContainerDashboardQueryDefinition queryDefinition = getNewQueryDefinition();
        filterParams.put(queryDefinition.getContainerInstanceFieldName(), SputumTrackingInstance.PreTreatment.name());
        return super.count(queryDefinition, filterParams);
    }
}
