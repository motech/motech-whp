package org.motechproject.whp.container.service;

import org.motechproject.couchdbcrud.model.CrudEntity;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class AlternateDiagnosisService extends CrudEntity<AlternateDiagnosis>{

    AllAlternateDiagnosis allAlternateDiagnosis;

    @Autowired
    public AlternateDiagnosisService(AllAlternateDiagnosis allAlternateDiagnosis) {
        this.allAlternateDiagnosis = allAlternateDiagnosis;
    }

    @Override
    public List<String> getDisplayFields() {
        return asList("name", "code");
    }

    @Override
    public List<String> getFilterFields() {
        return asList("name", "code");
    }

    @Override
    public CrudRepository<AlternateDiagnosis> getRepository() {
        return allAlternateDiagnosis;
    }

    @Override
    public Class getEntityType() {
        return AlternateDiagnosis.class;
    }

    @Override
    public String entityName() {
        return "AlternateDiagnosis";
    }
}
