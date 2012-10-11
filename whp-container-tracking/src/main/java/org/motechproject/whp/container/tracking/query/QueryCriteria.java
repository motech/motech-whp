package org.motechproject.whp.container.tracking.query;

import lombok.Getter;

@Getter
public class QueryCriteria implements Criteria {
    public static final String SEPARATOR = ":";
    private Field field;
    private String value;

    public QueryCriteria(Field field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String buildCriteriaString() {
        return field.getName() + SEPARATOR + value;
    }
}
