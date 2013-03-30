package org.motechproject.couchdbcrud.service;

import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;

import java.util.List;
import java.util.Map;

public abstract class CrudEntity<T extends MotechBaseDataObject> implements Paging {
    /**
     * Fields you want to display in the listing section
     * @return
     */
    public abstract List<String> getDisplayFields();

    /**
     * Fields you want to show as filters.
     * These fields should have respective "by_fieldName" views defined.
     * @return
     */
    public abstract List<String> getFilterFields();

    /**
     * The CrudRepository for this entity
     * @return
     */
    public abstract CrudRepository<T> getRepository();

    /**
     * Type of the entity
     * @return
     */
    public abstract Class getEntityType();

    public String entityName(){
        return this.getClass().getSimpleName();
    }

    public PageResults page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria) {
        List<T> result;
        int count;
        filterParams = filterParams.removeEmptyParams();

        if (filterParams.isEmpty()) {
            count = getRepository().count();
            result = getRepository().getAll((pageNumber - 1) * rowsPerPage, rowsPerPage);
        } else {
            Map.Entry<String, Object> entry = filterParams.entrySet().iterator().next();
            result = getRepository().findBy(entry.getKey(), String.valueOf(entry.getValue()));
            count = result.size();
            result = result.subList((pageNumber - 1) * rowsPerPage, Math.min(count, (pageNumber * rowsPerPage)));
        }

        return createPageResults(pageNumber, result, count);
    }

    private PageResults createPageResults(Integer pageNumber, List<T> result, int count) {
        PageResults results = new PageResults();
        results.setPageNo(pageNumber);
        results.setResults(result);
        results.setTotalRows(count);
        return results;
    }
}
