package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.apache.commons.lang.math.LongRange;

@Data
public class ContainerRange {

    public ContainerRange() {
    }

    public ContainerRange(long from, long to) {
        this.from = from;
        this.to = to;
    }

    private long from;
    private long to;

    public boolean includes(long id) {
        return new LongRange(from, to).containsLong(id);
    }
}
