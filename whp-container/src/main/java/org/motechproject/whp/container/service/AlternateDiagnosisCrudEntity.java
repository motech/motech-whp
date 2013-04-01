package org.motechproject.whp.container.service;

import org.motechproject.couchdbcrud.service.CouchDBCrudEntity;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class AlternateDiagnosisCrudEntity extends CouchDBCrudEntity<AlternateDiagnosis>{

    @Autowired
    public AlternateDiagnosisCrudEntity(AllAlternateDiagnosis allAlternateDiagnosis) {
        super(allAlternateDiagnosis);
    }

    @Override
    public List<String> getDisplayFields() {
        return asList("name", "code");
    }

    @Override
    public List<String> getFilterFields() {
        return asList("code");
    }

    @Override
    public Class getEntityType() {
        return AlternateDiagnosis.class;
    }
}
