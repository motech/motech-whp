package org.motechproject.whp.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ProviderRequest {

    @NotEmpty
    private String provider_id;

    @NotEmpty
    private String district;

    @NotEmpty
    private String primary_mobile;

    private String secondary_mobile;
    private String tertiary_mobile;

    private String username;
    private String password;
    private String uuid;

    @NotEmpty
    private String date;
}
