package org.motechproject.whp.domain;


import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class HomePageTest {

    @Test
    public void shouldBeTheOnlyPageInTheListOfCandidatePages() {
        HomePage homePage = new HomePage(asList("homePage"));
        assertEquals("homePage", homePage.getHomePage());
    }

    @Test
    public void shouldBeTheFirstPageInTheListOfCandidatePages() {
        HomePage homePage = new HomePage(asList("homePage1", "homePage2"));
        assertEquals("homePage1", homePage.getHomePage());
    }

    @Test
    public void shouldBeEmptyWhenThereAreNoCandidatePages() {
        HomePage homePage = new HomePage(Collections.<String>emptyList());
        assertTrue(homePage.getHomePage().isEmpty());
    }

    @Test
    public void shouldBeEmptyWhenCandidatePagesIsNull() {
        HomePage homePage = new HomePage(null);
        assertTrue(homePage.getHomePage().isEmpty());
    }
}
