package org.motechproject.whp.container.mapping.domain;

import lombok.Data;

@Data
public class ContainerRange {

    public ContainerRange() { }

    public ContainerRange(long from, long to) {
        this.from = from;
        this.to = to;
    }

    private long from;
    private long to;
}
