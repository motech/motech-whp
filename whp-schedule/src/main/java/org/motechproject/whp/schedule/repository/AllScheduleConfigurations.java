package org.motechproject.whp.schedule.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
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

    public void saveOrUpdate(ScheduleConfiguration configuration) {
        addOrReplace(configuration, "scheduleType", configuration.getScheduleType().name());
    }

    @View(name = "by_scheduleType", map = "function(doc) {if (doc.type ==='ScheduleConfiguration') {emit(doc.scheduleType, doc._id);}}")
    public ScheduleConfiguration withType(ScheduleType reminderType) {
        ViewQuery query = createQuery("by_scheduleType").key(reminderType).includeDocs(true);
        return singleResult(db.queryView(query, ScheduleConfiguration.class));
    }
}
