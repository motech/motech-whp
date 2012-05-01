package org.motechproject.whp.functional.data;


import lombok.Data;

@Data
public class TestProvider {

    private String providerId;
    private String password;

    public TestProvider(String providerId, String password) {
        this.providerId = providerId;
        this.password = password;
    }
}
