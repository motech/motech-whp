package org.motechproject.whp.refdata.objectcache;

import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.model.MotechBaseDataObject;

import java.util.*;

public abstract class ObjectCache<T extends MotechBaseDataObject> {

    private Class<T> type;
    protected HashMap<String, T> objectMap = new HashMap<String, T>();
    protected List<T> objectList = new ArrayList<T>();
    protected MotechBaseRepository<T> repository;

    public ObjectCache(MotechBaseRepository<T> repository, Class<T> type) {
        this.repository = repository;
        this.type = type;
        populateData();
    }

    protected String getKey(T object) {
        return object.getId();
    }

    public T getBy(String key) {
        return objectMap.get(key);
    }

    public void refresh() {
        objectMap.clear();
        objectList.clear();
        populateData();
    }

    public List<T> getAll() {
        return objectList;
    }

    private void populateData() {
        objectList = repository.getAll();
        for (T t : objectList) {
            this.objectMap.put(getKey(t), t);
        }
        if (Arrays.asList(type.getInterfaces()).contains(Comparable.class)) {
            List data = objectList;
            Collections.sort((List<Comparable>) data);
        }
    }
}
