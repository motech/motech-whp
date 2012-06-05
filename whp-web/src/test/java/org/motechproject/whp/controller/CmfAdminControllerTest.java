package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.builder.CmfAdminWebRequestBuilder;
import org.motechproject.whp.contract.CmfAdminWebRequest;
import org.motechproject.whp.patient.domain.CmfAdmin;
import org.motechproject.whp.patient.domain.CmfLocation;
import org.motechproject.whp.patient.repository.AllCmfAdmins;
import org.motechproject.whp.patient.repository.AllCmfLocations;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CmfAdminControllerTest {
    @Mock
    AllProviders allProviders;
    @Mock
    AllCmfAdmins allCmfAdmins;
    @Mock
    AllCmfLocations allCmfLocations;

    @Mock
    MotechAuthenticationService motechAuthenticationService;
    @Mock
    BindingResult bindingResult;
    CmfAdminController itAdminController;

    @Before
    public void setup() {
       initMocks(this);
       itAdminController = new CmfAdminController(allCmfLocations,allCmfAdmins,allProviders,motechAuthenticationService);
    }

    @Test
    public void shouldCreateCmfAccount() {
        CmfAdminWebRequest request= new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();

        when(allCmfLocations.findByLocation(request.getLocation())).thenReturn(new CmfLocation(request.getLocation()));
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        itAdminController.createCmfAdmin(request,bindingResult, uiModel);

        ArgumentCaptor<CmfAdmin> argumentCaptor = ArgumentCaptor.forClass(CmfAdmin.class);
        verify(allCmfAdmins,times(1)).addOrReplace(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getUserId(),request.getUserId());
        verify(motechAuthenticationService,times(1)).register(request.getUserId(),request.getPassword(),null, Arrays.asList(WHPRole.CMF_ADMIN.name()));


        assertEquals("Successfully created cmf admin with user id "+ request.getUserId(), uiModel.get("message"));
    }

    @Test
    public void shouldPopulateValidationErrorForUserId() {
        CmfAdminWebRequest request= new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request,"account");
        when(allCmfAdmins.findByUserId(request.getUserId())).thenReturn(new CmfAdmin());
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        String view = itAdminController.createCmfAdmin(request, bindingResult, uiModel);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("UserId already exists",bindingResult.getFieldError("userId").getDefaultMessage());
        assertEquals(request.getUserId(),bindingResult.getFieldError("userId").getRejectedValue());
        assertEquals(false,bindingResult.getFieldError("userId").isBindingFailure());

    }

    @Test
    public void shouldPopulateValidationErrorForMismatchPassword() {
        CmfAdminWebRequest request= new CmfAdminWebRequestBuilder().withUserName("test").withPassword("pwd1").withConfirmPassword("pwd2").build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request,"account");
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        String view = itAdminController.createCmfAdmin(request, bindingResult, uiModel);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("Should be same as password",bindingResult.getFieldError("confirmPassword").getDefaultMessage());

    }

    @Test
    public void shouldPopulateValidationErrorForLocation() {
        CmfAdminWebRequest request= new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request,"account");
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));
        when(allCmfLocations.findByLocation(request.getLocation())).thenReturn(null);
        String view = itAdminController.createCmfAdmin(request, bindingResult, uiModel);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("Location is not found",bindingResult.getFieldError("location").getDefaultMessage());

    }


    private void assertInvalidRequestPage(CmfAdminWebRequest request, BindingAwareModelMap uiModel, String view) {
        assertEquals("cmfadmin/create",view);

        List<String> locations = (List<String>)uiModel.get("locations");
        assertEquals(Arrays.asList("Delhi", "Patna"), locations);

        CmfAdminWebRequest webRequest = (CmfAdminWebRequest)uiModel.get("account");
        assertEquals(request, webRequest);

        verify(allCmfAdmins,never()).addOrReplace(any(CmfAdmin.class));
    }


}
