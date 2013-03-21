package org.motechproject.whp.schedule.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateHandlerRequest;
import org.ektorp.ViewQuery;
import org.ektorp.support.UpdateHandler;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllScheduleConfigurations extends MotechBaseRepository<ScheduleConfiguration> {

    @Autowired
    protected AllScheduleConfigurations(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(ScheduleConfiguration.class, db);
    }

    @UpdateHandler(name = "upsert", file = "upsert.js")
    public void saveOrUpdate(ScheduleConfiguration configuration) {
        UpdateHandlerRequest request = new UpdateHandlerRequest();
        request.designDocId(this.stdDesignDocumentId);
        request.docId(configuration.getId());
        request.body(configuration);
        request.functionName("upsert");
        db.callUpdateHandler(request);
    }

    @View(name = "with_type", map = "function(doc) {if (doc.type ==='ScheduleConfiguration') {emit(doc.scheduleType, doc._id);}}")
    public ScheduleConfiguration withType(ScheduleType reminderType) {
        ViewQuery query = createQuery("with_type").key(reminderType).includeDocs(true);
        return singleResult(db.queryView(query, ScheduleConfiguration.class));
    }
}
