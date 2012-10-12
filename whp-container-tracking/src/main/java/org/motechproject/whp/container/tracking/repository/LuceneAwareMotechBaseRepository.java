package org.motechproject.whp.container.tracking.repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.container.tracking.query.QueryBuilder;
import org.motechproject.whp.container.tracking.query.QueryDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class LuceneAwareMotechBaseRepository<T extends MotechBaseDataObject> extends MotechBaseRepository<T> {

    protected LuceneAwareMotechBaseRepository(Class<T> type, LuceneAwareCouchDbConnector db) {
        super(type, db);
    }

    public List<T> filter(QueryDefinition queryDefinition, String viewName, String searchFunctionName, Properties filterParams, Integer skip, Integer limit) {
        CustomLuceneResult luceneResult = getLuceneResult(queryDefinition, filterParams, viewName, searchFunctionName, limit, skip);
        List<CustomLuceneResult.Row<T>> resultRows = luceneResult.getRows();
        List<T> results = new ArrayList();
        for (CustomLuceneResult.Row<T> row : resultRows)
            results.add(row.getDoc());
        return results;
    }

    private CustomLuceneResult getLuceneResult(QueryDefinition queryDefinition, Properties queryParams, String viewName, String searchFunctionName, Integer limit, Integer skip) {
        LuceneQuery query = new LuceneQuery(viewName, searchFunctionName);
        String queryString = new QueryBuilder(queryParams, queryDefinition).build();
        query.setQuery(queryString.toString());
        query.setIncludeDocs(true);
        query.setLimit(limit);
        query.setSkip(skip);

        TypeReference resultDocType = getTypeReference();
        return ((LuceneAwareCouchDbConnector) db).queryLucene(query, resultDocType);
    }

    abstract TypeReference getTypeReference();
}
