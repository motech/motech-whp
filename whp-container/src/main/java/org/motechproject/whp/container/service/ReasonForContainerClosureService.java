package org.motechproject.whp.container.service;

import org.motechproject.couchdbcrud.model.CrudEntity;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class ReasonForContainerClosureService extends CrudEntity<ReasonForContainerClosure>{

    AllReasonForContainerClosures allReasonForContainerClosures;

    @Autowired
    public ReasonForContainerClosureService(AllReasonForContainerClosures allReasonForContainerClosures) {
        this.allReasonForContainerClosures = allReasonForContainerClosures;
    }

    @Override
    public List<String> getDisplayFields() {
        return asList("name", "code", "phase" , "applicableToAdmin");
    }

    @Override
    public List<String> getFilterFields() {
        return asList("code");
    }

    @Override
    public CrudRepository<ReasonForContainerClosure> getRepository() {
        return allReasonForContainerClosures;
    }

    @Override
    public Class getEntityType() {
        return ReasonForContainerClosure.class;
    }

    @Override
    public String entityName() {
        return "ReasonForContainerClosure";
    }
}
