package org.motechproject.whp.providerreminder.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllProviderReminderConfigurations extends MotechBaseRepository<ProviderReminderConfiguration> {

    @Autowired
    protected AllProviderReminderConfigurations(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(ProviderReminderConfiguration.class, db);
    }

    public void saveOrUpdate(ProviderReminderConfiguration configuration) {
        ProviderReminderConfiguration existingConfiguration = withType(configuration.getReminderType());
        if (null == existingConfiguration) {
            add(configuration);
        } else {
            update(existingConfiguration, configuration);
            update(configuration);
        }
    }

    @View(name = "with_type", map = "function(doc) {if (doc.type ==='ProviderReminderConfiguration') {emit(doc.reminderType, doc._id);}}")
    public ProviderReminderConfiguration withType(ProviderReminderType reminderType) {
        ViewQuery query = createQuery("with_type").key(reminderType).includeDocs(true);
        return singleResult(db.queryView(query, ProviderReminderConfiguration.class));
    }

    private ProviderReminderConfiguration update(ProviderReminderConfiguration existingConfiguration, ProviderReminderConfiguration configuration) {
        configuration.setId(existingConfiguration.getId());
        configuration.setRevision(existingConfiguration.getRevision());
        return configuration;
    }
}
