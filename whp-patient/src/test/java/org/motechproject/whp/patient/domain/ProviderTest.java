package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ProviderTest {
    @Test
    public void shouldSetProviderIdInLowercase() {
        Provider provider = new Provider();
        provider.setProviderId("QWER");
        assertEquals("qwer",provider.getProviderId());
    }

    @Test
    public void shouldCreatProviderWithIdInLowercase() {
        Provider provider = new Provider("QWE","","", DateTime.now());
        assertEquals("qwe",provider.getProviderId());
    }

    @Test
    public void shouldHandleNullValuesForId() {
        Provider provider=new Provider("","","",DateTime.now());
        provider.setProviderId(null);
        assertEquals(null,provider.getProviderId());

        provider = new Provider(null,"","",DateTime.now());
        assertEquals(null,provider.getProviderId());

    }
}
