package org.motechproject.whp.container.tracking.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QueryBuilder {
    public static final String AND = " AND ";
    private final Properties filterParams;
    private final QueryDefinition queryDefinition;

    public QueryBuilder(Properties filterParams, QueryDefinition queryDefinition) {
        this.filterParams = filterParams;
        this.queryDefinition = queryDefinition;
    }

    public String build() {
        List<Criteria> criterias = getQueryCriteria();
        return getQuery(criterias);
    }

    private String getQuery(List<Criteria> criterias) {
        StringBuilder builder = new StringBuilder();
        for (Criteria criteria : criterias) {
            builder.append(criteria.buildCriteriaString());
            builder.append(AND);
        }

        if(builder.length() == 0)
            return "";

        return builder.substring(0, builder.lastIndexOf(AND));
    }

    private List<Criteria> getQueryCriteria() {
        List<Criteria> criterias = new ArrayList<>();
        for (Field field : queryDefinition.queryableFields()) {
            if (filterParams.containsKey(field.getName()))
                addQueryCriteria(criterias, field);
            else if (containsRangeField(field)) {
                addRangeCriteria(criterias, field);
            }
        }
        return criterias;
    }

    private boolean containsRangeField(Field field) {
        return field.isAllowsRange() && filterParams.containsKey(field.getFromName()) && filterParams.containsKey(field.getToName());
    }

    private void addRangeCriteria(List<Criteria> criterias, Field field) {
        String from = filterParams.get(field.getFromName()).toString();
        String to = filterParams.get(field.getToName()).toString();
        criterias.add(new RangeCriteria(field, from, to));
    }

    private void addQueryCriteria(List<Criteria> criterias, Field field) {
        criterias.add(new QueryCriteria(field, filterParams.get(field.getName()).toString()));
    }
}
