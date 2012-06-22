package org.motechproject.whp.uimodel;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateCMFAdminRequest {

    private String id;

    private String userId;

    @Size(min = 1,message = "Email ID cannot be empty")
    @Pattern(regexp = "$|^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$" , message = "Please enter a valid email id. Example: abc@xyz.com")
    private String email;

    private String department;

    @Size(min = 1, message = "Location cannot be empty")
    private String locationId;

    @Size(min = 1, message="Staff name cannot be empty")
    private String staffName;
}
