package org.motechproject.whp.container.tracking.query;

import lombok.Getter;

import java.util.Properties;

@Getter
public class QueryField implements Field {
    private String name;
    private FieldType type;

    public QueryField(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean presentIn(Properties filterParams) {
        return filterParams.containsKey(getName());
    }

    @Override
    public Criteria createCriteria(Properties filterParams) {
        return new QueryCriteria(this, filterParams.get(getName()).toString());
    }
}
