package org.motechproject.whp.controller;

import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.contract.CmfAdminWebRequest;
import org.motechproject.whp.patient.domain.CmfAdmin;
import org.motechproject.whp.patient.domain.CmfLocation;
import org.motechproject.whp.patient.repository.AllCmfAdmins;
import org.motechproject.whp.patient.repository.AllCmfLocations;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.springframework.util.StringUtils.hasText;

@Controller
@RequestMapping(value = "/cmfAdmin")
public class CmfAdminController {


    AllCmfLocations allCmfLocations;
    AllCmfAdmins allCmfAdmins;
    AllProviders allProviders;
    MotechAuthenticationService motechAuthenticationService;
    private static final String LOCATION_LIST = "locations";
    private static final String CREATE_CMF_ADMIN_MODEL_NAME = "account";
    private static final String NOTIFICATION_MESSAGE = "message";

    @Autowired
    public CmfAdminController(AllCmfLocations allCmfLocations, AllCmfAdmins allCmfAdmins, AllProviders allProviders, MotechAuthenticationService motechAuthenticationService) {
        this.allCmfLocations = allCmfLocations;
        this.allCmfAdmins = allCmfAdmins;
        this.allProviders = allProviders;
        this.motechAuthenticationService = motechAuthenticationService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String loadcreateCmfAdminPage(Model uiModel) {
        setUpModel(uiModel);
        uiModel.addAttribute(CREATE_CMF_ADMIN_MODEL_NAME, new CmfAdminWebRequest());
        return "cmfadmin/create";
    }

    private void setUpModel(Model uiModel) {
        List<CmfLocation> cmfLocations = allCmfLocations.getAll();
        uiModel.addAttribute(LOCATION_LIST, extract(cmfLocations, on(CmfLocation.class).getLocation()));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createCmfAdmin(@ModelAttribute("account") @Valid CmfAdminWebRequest request, BindingResult bindingResult, Model uiModel) {
        if (isValid(bindingResult, request)) {
                CmfLocation cmfLocation = allCmfLocations.findByLocation(request.getLocation());
                if (cmfLocation != null) {
                    String locationId = cmfLocation.getLocation();
                        try {
                            CmfAdmin admin = new CmfAdmin(request.getUserId(), request.getEmail(), request.getDepartment(), locationId);
                            allCmfAdmins.addOrReplace(admin);
                            motechAuthenticationService.register(admin.getUserId(), request.getPassword(), null, Arrays.asList(WHPRole.CMF_ADMIN.name()));
                            uiModel.addAttribute(NOTIFICATION_MESSAGE,"Successfully created cmf admin with user id " + request.getUserId());
                            return "itadmin/itadmin";
                        } catch (Exception e) {
                            bindingResult.addError(new ObjectError(CREATE_CMF_ADMIN_MODEL_NAME,e.getMessage()));
                        }
                } else {
                    bindingResult.addError(new FieldError(CREATE_CMF_ADMIN_MODEL_NAME,"location", "Location is not found"));
                }
        }
        setUpModel(uiModel);
        uiModel.addAttribute(CREATE_CMF_ADMIN_MODEL_NAME, request);
        return "cmfadmin/create";
    }

    private boolean isValid(BindingResult bindingResult, CmfAdminWebRequest request) {
        if(hasText(request.getConfirmPassword()) && !request.getConfirmPassword().equals(request.getPassword()))
            bindingResult.addError(new FieldError(CREATE_CMF_ADMIN_MODEL_NAME,"confirmPassword","Should be same as password"));

        if(hasText(request.getUserId()) && (allCmfAdmins.findByUserId(request.getUserId()) != null || allProviders.findByProviderId(request.getUserId()) != null )){
            FieldError error = new FieldError(CREATE_CMF_ADMIN_MODEL_NAME, "userId", request.getUserId(), false, new String[]{}, new String[]{}, "UserId already exists");
            bindingResult.addError(error);
        }
        return !bindingResult.hasErrors();
    }

}
