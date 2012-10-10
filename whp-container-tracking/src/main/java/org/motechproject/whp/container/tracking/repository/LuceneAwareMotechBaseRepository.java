package org.motechproject.whp.container.tracking.repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LuceneAwareMotechBaseRepository<T extends MotechBaseDataObject> extends MotechBaseRepository<T> {

    protected LuceneAwareMotechBaseRepository(Class<T> type, LuceneAwareCouchDbConnector db) {
        super(type, db);
    }

    public List<T> filter(Map<String, String> queryParams, Integer skip, Integer limit, String viewName, String searchFunctionName){
        CustomLuceneResult luceneResult = getLuceneResult(queryParams, skip, limit, viewName, searchFunctionName);
        List<CustomLuceneResult.Row<T>> resultRows = luceneResult.getRows();

        List<T> results = new ArrayList();
        for(CustomLuceneResult.Row<T> row : resultRows)
            results.add(row.getDoc());
        return results;
    }

    private CustomLuceneResult getLuceneResult(Map<String, String> queryParams, Integer skip, Integer limit, String viewName, String searchFunctionName) {
        LuceneQuery query = new LuceneQuery(viewName, searchFunctionName);

        String queryString = buildQueryString(queryParams);
        query.setQuery(queryString.toString());
        query.setIncludeDocs(true);
        query.setLimit(limit);
        query.setSkip(skip);
        TypeReference resultDocType = getTypeReference();
        return ((LuceneAwareCouchDbConnector) db).queryLucene(query, resultDocType);
    }

    abstract TypeReference getTypeReference();

    private String buildQueryString(Map<String, String> queryParams) {
        StringBuilder queryString = new StringBuilder();
        for(String queryParam : queryParams.keySet()){
            queryString.append(queryParam);
            queryString.append(":");
            queryString.append(queryParams.get(queryParam));
            queryString.append(" AND ");
        }

        return queryString.substring(0, queryString.length() - 5);
    }
}
