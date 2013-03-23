package org.motechproject.couchdbcrud.service;

import org.motechproject.couchdbcrud.model.CrudEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class CrudService {
    Map<String, CrudEntity> allCrudEntities;

    @Autowired
    public CrudService(Set<CrudEntity> crudEntities) {
        allCrudEntities = new HashMap<>();
        for(CrudEntity crudEntity : crudEntities){
            allCrudEntities.put(crudEntity.entityName(), crudEntity);
        }
    }

    public CrudEntity getEntity(String name){
        return allCrudEntities.get(name);
    }
}
