package org.motechproject.whp.container.tracking.query;

public class RangeCriteria implements Criteria {
    private Field field;
    private String from;
    private String to;

    public RangeCriteria(Field field, String from, String to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    @Override
    public String buildCriteriaString() {
        return String.format("%s<%s>:[%s TO %s]", field.getName(), field.getType().getValue(), from, to);
    }
}
