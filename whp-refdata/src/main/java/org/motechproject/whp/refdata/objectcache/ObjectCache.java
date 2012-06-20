package org.motechproject.whp.refdata.objectcache;

import org.motechproject.dao.MotechBaseRepository;

import java.util.*;

public abstract class ObjectCache<T> {

    protected HashMap<String, T> objectMap = new HashMap<String, T>();

    protected List<T> objectList = new ArrayList<T>();

    protected MotechBaseRepository repository;
    private Class<T> type;

    public ObjectCache(MotechBaseRepository repository, Class<T> type) {
        this.repository = repository;
        this.type = type;
        populateData();
    }

    protected abstract String getKey(T t);

    public T getBy(String key){
        return objectMap.get(key);
    }

    public void refresh(){
        objectMap.clear();
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
        if(Arrays.asList(type.getInterfaces()).contains(Comparable.class)) {
            Collections.sort((List<Comparable>) objectList);
        }
    }

}
