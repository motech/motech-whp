package org.motechproject.whp.patient.repository;

import org.junit.Test;
import org.motechproject.whp.provider.domain.WHPProvider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = "classpath*:/applicationProviderContext.xml")
public class AllProvidersTest extends SpringIntegrationTest {
    @Autowired
    AllProviders allProviders;

    @Test
    public void shouldSaveProviderInfo(){
        WHPProvider provider = new WHPProvider("P00001", "984567876");
        allProviders.add(provider);

        WHPProvider providerReturned = allProviders.findByProviderId("P00001");
        assertNotNull(providerReturned);
        assertEquals("984567876",providerReturned.getPrimaryMobile());
    }

}
