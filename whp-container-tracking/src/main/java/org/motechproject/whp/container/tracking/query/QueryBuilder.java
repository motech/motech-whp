package org.motechproject.whp.container.tracking.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QueryBuilder {
    public static final String AND = " AND ";
    public static final String EMPTY_STRING = "";
    private final Properties filterParams;
    private final QueryDefinition queryDefinition;

    public QueryBuilder(Properties filterParams, QueryDefinition queryDefinition) {
        this.filterParams = filterParams;
        this.queryDefinition = queryDefinition;
    }

    public String build() {
        List<Criteria> criteria = getQueryCriteria();
        return getQuery(criteria);
    }

    private String getQuery(List<Criteria> criteria) {
        StringBuilder builder = new StringBuilder();
        for (Criteria criterion : criteria) {
            builder.append(criterion.buildCriteriaString());
            builder.append(AND);
        }

        if(builder.length() == 0)
            return EMPTY_STRING;

        return builder.substring(0, builder.lastIndexOf(AND));
    }

    private List<Criteria> getQueryCriteria() {
        List<Criteria> criteria = new ArrayList<>();
        for (Field field : queryDefinition.getFields()) {
            if(field.presentIn(filterParams)) {
                criteria.add(field.createCriteria(filterParams));
            }
        }
        return criteria;
    }
}
