package org.motechproject.whp.controller;

import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class UserController extends BaseWebController {

    private UserService userService;
    private Properties whpProperties;

    @Autowired
    public UserController(UserService userService, Properties whpProperties) {
        this.userService = userService;
        this.whpProperties = whpProperties;
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request) throws IOException {
        setVersion(request.getSession());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/changePassword")
    @ResponseBody
    public String changePassword(String currentPassword, String newPassword, HttpServletRequest request) {
        MotechUser authenticatedUser = userService.changePassword(loggedInUser(request).getUserName(), currentPassword, newPassword);
        if (authenticatedUser == null)
            return "'Current Password' you entered is incorrect";
        request.getSession().setAttribute(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser);
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/activateUser")
    @ResponseBody
    public String activateUser(@RequestParam(value = "userName") String providerId, String newPassword) {
        boolean hasResetPassword = userService.resetPassword(providerId, newPassword);
        if (hasResetPassword)
            userService.activateUser(providerId);
        else
            return "User does not exist";

        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resetPassword")
    @ResponseBody
    public String resetPassword(@RequestParam(value = "userName") String userName) {
        boolean successful = userService.resetPassword(userName, whpProperties.get("password.default").toString());
        if (!successful)
            return "User does not exist";
        return "";
    }

    private void setVersion(HttpSession session) throws IOException {
        if (session.getAttribute("version") != null) return;
        String version = "0";
        InputStream stream = session.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
        if (stream != null) {
            Properties prop = new Properties();
            prop.load(stream);
            String versionProperty = prop.getProperty("Build-Number");
            version = versionProperty == null ? "0" : versionProperty;
        }
        session.setAttribute("version", version);
    }
}

