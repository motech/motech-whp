package org.motechproject.whp.controller;

import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class UserController extends BaseController {

    private MotechAuthenticationService motechAuthenticationService;

    @Autowired
    public UserController(MotechAuthenticationService motechAuthenticationService) {
        this.motechAuthenticationService = motechAuthenticationService;
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request) throws IOException {
        setVersion(request.getSession());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/changePassword")
    @ResponseBody
    public String changePassword(String newPassword, HttpServletRequest request) {
        MotechWebUser authenticatedUser = motechAuthenticationService.changePassword(loggedInUser(request).getUserName(), newPassword);
        request.getSession().setAttribute(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser);
        return "";
    }

    private void setVersion(HttpSession session) throws IOException {
        if (session.getAttribute("version") != null) return;
        String version = "0";
        InputStream stream = session.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
        if (stream != null) {
            Properties prop = new Properties();
            prop.load(stream);
            String versionProperty = prop.getProperty("Hudson-Build-Number");
            version = versionProperty == null ? "0" : versionProperty;
        }
        session.setAttribute("version", version);
    }
}

