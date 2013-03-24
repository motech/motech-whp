package org.motechproject.couchdbcrud.service;

import org.motechproject.couchdbcrud.model.CrudEntity;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;
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
        for (CrudEntity crudEntity : crudEntities) {
            allCrudEntities.put(crudEntity.entityName(), crudEntity);
        }
    }

    public CrudEntity getEntity(String name) {
        return allCrudEntities.get(name);
    }

    public void deleteEntity(String name, String entityId) {
        CrudRepository repository = allCrudEntities.get(name).getRepository();
        repository.remove(repository.get(entityId));
    }


    public void saveEntity(String name, MotechBaseDataObject entity) {
        CrudRepository repository = getEntity(name).getRepository();
        if (entity.getId() == null) {
            repository.add(entity);
        } else {
            repository.update(entity);
        }
    }

    public Object getEntity(String entityName, String entityId) {
        CrudRepository repository = getEntity(entityName).getRepository();
        return repository.get(entityId);
    }
}
