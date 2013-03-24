package org.motechproject.couchdbcrud.model;

import org.apache.commons.lang.StringUtils;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;

import java.util.List;
import java.util.Map;

public abstract class CrudEntity<T extends MotechBaseDataObject> implements Paging {
    public abstract List<String> getDisplayFields();
    public abstract List<String> getFilterFields();
    public abstract CrudRepository<T> getRepository();
    public abstract Class getEntityType();

    public PageResults page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria) {
        List<T> result = null;
        int count = 0;

        if (filterParams.isEmpty()) {
            count = getRepository().count();
            result = getRepository().getAll((pageNumber - 1) * rowsPerPage, rowsPerPage);
        } else {
            for(Map.Entry<String, Object> entry : filterParams.entrySet()){
                if(StringUtils.isNotBlank(String.valueOf(entry.getValue()))){
                    result = getRepository().findBy(entry.getKey(), String.valueOf(entry.getValue()));
                    count = result.size();
                    result = result.subList((pageNumber - 1) * rowsPerPage, Math.min(count, (pageNumber * rowsPerPage)));
                    break;
                }
            }

        }

        PageResults results = new PageResults();
        results.setPageNo(pageNumber);
        results.setResults(result);
        results.setTotalRows(count);

        return results;
    }
}
