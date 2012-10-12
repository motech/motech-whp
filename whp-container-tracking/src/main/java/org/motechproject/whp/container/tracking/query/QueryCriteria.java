package org.motechproject.whp.container.tracking.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class QueryCriteria implements Criteria {
    static final String QUERY_SEPARATOR = ":";
    private QueryField field;
    private String value;

    public QueryCriteria(QueryField field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String buildCriteriaString() {
        return field.getName() + QUERY_SEPARATOR + value;
    }
}
