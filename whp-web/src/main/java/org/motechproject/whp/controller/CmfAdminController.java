package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.request.CreateCMFAdminRequest;
import org.motechproject.whp.request.UpdateCMFAdminRequest;
import org.motechproject.whp.refdata.domain.CmfLocation;
import org.motechproject.whp.refdata.repository.AllCmfLocations;
import org.motechproject.whp.user.service.CmfAdminService;
import org.motechproject.whp.user.domain.CmfAdmin;
import org.motechproject.whp.user.repository.AllCmfAdmins;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.springframework.util.StringUtils.hasText;

@Controller
@RequestMapping(value = "/cmfAdmin")
public class CmfAdminController extends BaseController{

    AllCmfLocations allCmfLocations;
    AllCmfAdmins allCmfAdmins;
    AllProviders allProviders;
    MotechAuthenticationService motechAuthenticationService;
    CmfAdminService cmfAdminService;

    private static final String LOCATION_LIST = "locations";
    private static final String CREATE_CMF_ADMIN_MODEL_NAME = "account";
    private static final String NOTIFICATION_MESSAGE = "message";
    private static final String ALL_CMF_ADMINS = "allCmfAdmins";

    @Autowired
    public CmfAdminController(AllCmfLocations allCmfLocations, AllProviders allProviders, AllCmfAdmins allCmfAdmins, CmfAdminService cmfAdminService, MotechAuthenticationService motechAuthenticationService) {
        this.allCmfLocations = allCmfLocations;
        this.allProviders = allProviders;
        this.allCmfAdmins = allCmfAdmins;
        this.cmfAdminService = cmfAdminService;
        this.motechAuthenticationService = motechAuthenticationService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String form(Model uiModel) {
        addAllCmfLocationToUIModel(uiModel);
        uiModel.addAttribute(CREATE_CMF_ADMIN_MODEL_NAME, new CreateCMFAdminRequest());
        return "cmfadmin/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("account") @Valid CreateCMFAdminRequest createRequest, BindingResult bindingResult, Model uiModel, HttpServletRequest request) {
        if (isValid(bindingResult, createRequest)) {
            CmfLocation cmfLocation = allCmfLocations.findByLocation(createRequest.getLocation());
            if (cmfLocation != null) {
                String locationId = cmfLocation.getLocation();
                try {
                    CmfAdmin admin = new CmfAdmin(createRequest.getUserId().trim(), createRequest.getEmail(), createRequest.getDepartment(), locationId, createRequest.getStaffName());
                    cmfAdminService.add(admin, createRequest.getPassword());
                    Flash.out(NOTIFICATION_MESSAGE, "Successfully created cmf admin with user id " + createRequest.getUserId(), request);
                    return "redirect:/cmfAdmin/list";
                } catch (Exception e) {
                    bindingResult.addError(new ObjectError(CREATE_CMF_ADMIN_MODEL_NAME, e.getMessage()));
                }
            } else {
                bindingResult.addError(new FieldError(CREATE_CMF_ADMIN_MODEL_NAME, "location", "Location is not found"));
            }
        }
        addAllCmfLocationToUIModel(uiModel);
        uiModel.addAttribute(CREATE_CMF_ADMIN_MODEL_NAME, createRequest);
        return "cmfadmin/create";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model uiModel, HttpServletRequest request) {
        String message = Flash.in(NOTIFICATION_MESSAGE, request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute(NOTIFICATION_MESSAGE, message);
        }
        queryAndPopulateAllCmfAdminsInModel(uiModel);
        return "cmfadmin/list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editForm(@RequestParam("userId") String userId, Model uiModel) {
        CmfAdmin cmfAdmin = allCmfAdmins.findByUserId(userId);
        addAllCmfLocationToUIModel(uiModel);
        uiModel.addAttribute("cmfAdmin", cmfAdmin);
        return "cmfadmin/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute("cmfAdmin") @Valid UpdateCMFAdminRequest updateRequest, BindingResult bindingResult, Model uiModel, HttpServletRequest request) {
        CmfAdmin cmfAdmin = allCmfAdmins.get(updateRequest.getId());
        if (bindingResult.hasErrors()) {
            addAllCmfLocationToUIModel(uiModel);
            return "cmfadmin/edit";
        } else {
            Flash.out(NOTIFICATION_MESSAGE, "Successfully updated cmf admin with user id " + cmfAdmin.getUserId(), request);
            allCmfAdmins.updateDetails(cmfAdmin, updateRequest.getStaffName(), updateRequest.getLocationId(), updateRequest.getEmail(), updateRequest.getDepartment());
            return "redirect:/cmfAdmin/list";
        }
    }

    private void addAllCmfLocationToUIModel(Model uiModel) {
        List<CmfLocation> cmfLocations = allCmfLocations.getAll();
        uiModel.addAttribute(LOCATION_LIST, extract(cmfLocations, on(CmfLocation.class).getLocation()));
    }

    private void queryAndPopulateAllCmfAdminsInModel(Model uiModel) {
        List<CmfAdmin> cmfAdmins = allCmfAdmins.list();
        uiModel.addAttribute(ALL_CMF_ADMINS, cmfAdmins);
    }

    private boolean isValid(BindingResult bindingResult, CreateCMFAdminRequest request) {
        if (hasText(request.getConfirmPassword()) && !request.getConfirmPassword().equals(request.getPassword()))
            bindingResult.addError(new FieldError(CREATE_CMF_ADMIN_MODEL_NAME, "confirmPassword", "Should be same as password"));

        if (motechAuthenticationService.hasUser(request.getUserId())) {
            FieldError error = new FieldError(CREATE_CMF_ADMIN_MODEL_NAME, "userId", request.getUserId(), false, new String[]{}, new String[]{}, "UserId already exists");
            bindingResult.addError(error);
        }
        return !bindingResult.hasErrors();
    }

}
