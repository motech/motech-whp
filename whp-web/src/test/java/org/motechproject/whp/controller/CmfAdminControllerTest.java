package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.builder.CmfAdminWebRequestBuilder;
import org.motechproject.whp.common.domain.CmfLocation;
import org.motechproject.whp.common.repository.AllCmfLocations;
import org.motechproject.whp.request.CreateCMFAdminRequest;
import org.motechproject.whp.user.domain.CmfAdmin;
import org.motechproject.whp.user.repository.AllCmfAdmins;
import org.motechproject.whp.user.service.CmfAdminService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CmfAdminControllerTest {

    @Mock
    Model uiModel;
    @Mock
    CmfAdminService cmfAdminService;
    @Mock
    AllCmfLocations allCmfLocations;
    @Mock
    AllCmfAdmins allCmfAdmins;
    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    MotechAuthenticationService motechAuthenticationService;
    @Mock
    BindingResult bindingResult;
    CmfAdminController itAdminController;

    @Before
    public void setup() {
        initMocks(this);
        itAdminController = new CmfAdminController(allCmfLocations, allCmfAdmins, cmfAdminService, motechAuthenticationService);
    }

    @Test
    public void shouldLoadProviderSearchPage_verifyViewMappingForGET() throws Exception {
        String viewName = itAdminController.list(uiModel, httpServletRequest);
        assertEquals("cmfadmin/list", viewName);
    }

    @Test
    public void shouldCreateCmfAccount() throws WebSecurityException {
        CreateCMFAdminRequest request = new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();

        when(allCmfLocations.findByLocation(request.getLocation())).thenReturn(new CmfLocation(request.getLocation()));
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        itAdminController.create(request, bindingResult, uiModel, httpServletRequest);

        ArgumentCaptor<CmfAdmin> argumentCaptor = ArgumentCaptor.forClass(CmfAdmin.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(cmfAdminService, times(1)).add(argumentCaptor.capture(), passwordCaptor.capture());
        assertEquals(argumentCaptor.getValue().getUserId(), request.getUserId());
    }

    @Test
    public void shouldPopulateValidationErrorForUserId() throws WebSecurityException {
        CreateCMFAdminRequest request = new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request, "account");
        when(motechAuthenticationService.hasUser(request.getUserId())).thenReturn(true);
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        String view = itAdminController.create(request, bindingResult, uiModel, httpServletRequest);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("UserId already exists", bindingResult.getFieldError("userId").getDefaultMessage());
        assertEquals(request.getUserId(), bindingResult.getFieldError("userId").getRejectedValue());
        assertEquals(false, bindingResult.getFieldError("userId").isBindingFailure());

    }

    @Test
    public void shouldPopulateValidationErrorForMismatchPassword() throws WebSecurityException {
        CreateCMFAdminRequest request = new CmfAdminWebRequestBuilder().withUserName("test").withPassword("pwd1").withConfirmPassword("pwd2").build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request, "account");
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        String view = itAdminController.create(request, bindingResult, uiModel, httpServletRequest);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("Should be same as password", bindingResult.getFieldError("confirmPassword").getDefaultMessage());

    }

    @Test
    public void userIdShouldBeTrimmedWhileSaving() throws WebSecurityException {
        CreateCMFAdminRequest request = new CmfAdminWebRequestBuilder().withDefaults().withUserName("  cmfadmin  ").build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();

        when(allCmfLocations.findByLocation(request.getLocation())).thenReturn(new CmfLocation(request.getLocation()));
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));

        itAdminController.create(request, bindingResult, uiModel, httpServletRequest);

        ArgumentCaptor<CmfAdmin> argumentCaptor = ArgumentCaptor.forClass(CmfAdmin.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(cmfAdminService, times(1)).add(argumentCaptor.capture(), passwordCaptor.capture());
        assertEquals("cmfadmin", argumentCaptor.getValue().getUserId());
    }

    @Test
    public void shouldPopulateValidationErrorForLocation() throws WebSecurityException {
        CreateCMFAdminRequest request = new CmfAdminWebRequestBuilder().withDefaults().build();
        BindingAwareModelMap uiModel = new BindingAwareModelMap();
        BindingResult bindingResult = new DirectFieldBindingResult(request, "account");
        when(allCmfLocations.getAll()).thenReturn(Arrays.asList(new CmfLocation("Delhi"), new CmfLocation("Patna")));
        when(allCmfLocations.findByLocation(request.getLocation())).thenReturn(null);
        String view = itAdminController.create(request, bindingResult, uiModel, httpServletRequest);

        assertInvalidRequestPage(request, uiModel, view);

        assertNotNull("Location is not found", bindingResult.getFieldError("location").getDefaultMessage());

    }

    private void assertInvalidRequestPage(CreateCMFAdminRequest request, BindingAwareModelMap uiModel, String view) throws WebSecurityException {
        assertEquals("cmfadmin/create", view);

        List<String> locations = (List<String>) uiModel.get("locations");
        assertEquals(Arrays.asList("Delhi", "Patna"), locations);

        CreateCMFAdminRequest webRequest = (CreateCMFAdminRequest) uiModel.get("account");
        assertEquals(request, webRequest);

        verify(cmfAdminService, never()).add(any(CmfAdmin.class), anyString());
    }

}
