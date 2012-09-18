package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.importer.annotation.ColumnName;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.validation.constraints.DecimalMin;

@Data
public class ContainerMappingRequest {

    public ContainerMappingRequest() { }

    public ContainerMappingRequest(String providerId, String containerIdFrom, String containerIdTo) {
        this.providerId = providerId;
        this.containerIdFrom = containerIdFrom;
        this.containerIdTo = containerIdTo;
    }

    @NotNullOrEmpty
    @ColumnName(name = "Provider ID")
    private String providerId;

    @NotNullOrEmpty
    @DecimalMin(value = "0", message = "Container ID From must be decimal and must be >= 0")
    @ColumnName(name = "Container ID From")
    private String containerIdFrom;

    @NotNullOrEmpty
    @DecimalMin(value = "0", message = "Container ID To must be decimal and must be >= 0")
    @ColumnName(name = "Container ID To")
    private String containerIdTo;
}
