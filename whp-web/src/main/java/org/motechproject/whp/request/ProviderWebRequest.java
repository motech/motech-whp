package org.motechproject.whp.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;

@Data
public class ProviderWebRequest {

    @NotEmpty
    private String provider_id;

    @NotEmpty
    private String district;

    @NotEmpty
    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should have 10 digits")
    private String primary_mobile;

    private String secondary_mobile;

    private String tertiary_mobile;

    private String username;
    private String password;
    private String uuid;

    @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private String date;
}
