package org.motechproject.whp.container.tracking.query;

import lombok.Getter;

@Getter
public class Field {
    private String name;
    private FieldType type;
    private String fromName;
    private String toName;
    private boolean allowsRange;

    public Field(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public Field(String name, FieldType type, String fromName, String toName) {
        this(name, type);
        this.fromName = fromName;
        this.toName = toName;
        this.allowsRange = true;
    }

}
