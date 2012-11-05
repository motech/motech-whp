package org.motechproject.whp.it.controller;

import org.junit.Test;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:META-INF/spring/applicationContext.xml")
public class ContainerRegistrationCallLogControllerIT extends SpringIntegrationTest {

    @Test
    public void shouldPublishCallLogRequest() {




    }
}
