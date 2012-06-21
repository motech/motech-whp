package org.motechproject.whp.builder;

import org.motechproject.whp.uimodel.CreateCMFAdminRequest;

public class CmfAdminWebRequestBuilder {
    private CreateCMFAdminRequest request = new CreateCMFAdminRequest();
    public CmfAdminWebRequestBuilder withDefaults() {
       request.setUserId("cmfadmin1");
       request.setPassword("password");
       request.setConfirmPassword("password");
       request.setDepartment("department1");
       request.setLocation("Delhi");
       request.setEmail("a@b.com");
        return this;
    }

    public CreateCMFAdminRequest build(){
        return request;
    }

    public CmfAdminWebRequestBuilder withUserName(String userName) {
        request.setUserId(userName);
        return this;
    }
    public CmfAdminWebRequestBuilder withPassword(String password) {
        request.setPassword(password);
        return this;
    }

    public CmfAdminWebRequestBuilder withConfirmPassword(String password) {
        request.setConfirmPassword(password);
        return this;
    }
}
