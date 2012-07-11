package org.motechproject.whp.functional.data;

import lombok.Data;

@Data
public class TestCmfAdmin {
    String userId;
    String password;
    String location;
    String emailId;
    String staffName;
    String department;

    public TestCmfAdmin(String userId, String password, String location, String emailId, String staffName, String department) {

        this.userId = userId;
        this.password = password;
        this.location = location;
        this.emailId = emailId;
        this.staffName = staffName;
        this.department = department;
    }
}
