package org.motechproject.whp.container.tracking.query;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RangeCriteria implements Criteria {

    private RangeField field;
    private String from;
    private String to;

    public RangeCriteria(RangeField field, String from, String to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    @Override
    public String buildCriteriaString() {
        return String.format("%s<%s>:[%s TO %s]", field.getName(), field.getType().getValue(), from, to);
    }
}
