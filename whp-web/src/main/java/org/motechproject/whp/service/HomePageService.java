package org.motechproject.whp.service;

import org.motechproject.whp.domain.HomePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;

@Service
public class HomePageService {

    private Properties userHomePageMap;

    @Autowired
    public HomePageService(@Qualifier("homePageMappings") Properties homePageMappings) {
        this.userHomePageMap = homePageMappings;
    }

    public HomePage homePageFor(String role) {
        return new HomePage(asList(userHomePageMap.getProperty(role, "")));
    }

    public HomePage homePageFor(List<String> userRoles) {
        HomePage homePage = new HomePage();
        for (String userRole : userRoles) {
            homePage.add(userHomePageMap.getProperty(userRole));
        }
        return homePage;
    }
}
