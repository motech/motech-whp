package org.motechproject.whp.functional.framework.testdata;

import org.motechproject.util.DateUtil;

public abstract class TestEntity {
    private String documentId;

    protected static String unique(String string) {
        return string + DateUtil.now().toInstant().getMillis() + Math.random();
    }

    public String id() {
        return documentId;
    }

    public TestEntity id(String id) {
        this.documentId = id;
        return this;
    }

    public abstract String resourceName();
}
