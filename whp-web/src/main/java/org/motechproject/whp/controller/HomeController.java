package org.motechproject.whp.controller;

import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class HomeController {

    private AllProviders allProviders;

    @Autowired
    public HomeController(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @RequestMapping("/")
    public String homePage(HttpServletRequest request) {
        AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
        if (user.getRoles().hasRole(WHPRole.ADMIN.name())) {
            return "admin";
        } else {
            return "redirect:/patients?provider=" + allProviders.get(user.getExternalId()).getProviderId();
        }
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request) throws IOException {
        setVersion(request.getSession());
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

