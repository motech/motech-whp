package org.motechproject.whp.contract;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CmfAdminWebRequest {

    @Size(min = 1, message = "UserId cannot be empty")
    private String userId;

    @Pattern(regexp = "^$|[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$" , message = "Please enter a valid email id")
    private String email;

    private String department;

    @Size(min = 1, message = "location cannot be empty")
    private String location;

    @Size(min = 1,message = "Password cannot be empty")
    private String password;

    @Size(min = 1, message="Confirm Password cannot be empty")
    private String confirmPassword;
}
