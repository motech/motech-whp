package org.motechproject.whp.container.tracking.query;

import java.util.Properties;

public interface Field {
    boolean presentIn(Properties filterParams);

    Criteria createCriteria(Properties filterParams);
}
