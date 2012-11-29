package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class HomePageServiceTest {

    private HomePageService homePageService;
    private Properties homePageMappings;

    @Before
    public void setup() {
        homePageMappings = new Properties();
        homePageService = new HomePageService(homePageMappings);
    }

    @Test
    public void shouldReturnTheHomePageBasedOnRole() {
        String homePage = "homePage";
        String userRole = "userRole";

        homePageMappings.setProperty(userRole, homePage);
        assertEquals(homePage, homePageService.homePageFor(userRole).getHomePage());
    }

    @Test
    public void shouldReturnEmptyStringAsHomePageForInvalidRole() {
        String homePage = "homePage";
        String invalidUserRole = "invalidUserRole";
        String validUserRole = "userRole";

        homePageMappings.setProperty(validUserRole, homePage);
        assertEquals("", homePageService.homePageFor(invalidUserRole).getHomePage());
    }

    @Test
    public void shouldReturnHomePageGivenMultipleRoles() {
        List<String> userRoles = asList("role1", "role2");

        homePageMappings.setProperty(userRoles.get(0), "homePage1");
        homePageMappings.setProperty(userRoles.get(1), "homePage2");

        assertEquals("homePage1", homePageService.homePageFor(userRoles).getHomePage());
    }
}
