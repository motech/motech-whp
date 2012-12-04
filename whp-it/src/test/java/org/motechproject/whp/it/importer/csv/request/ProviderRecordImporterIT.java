package org.motechproject.whp.it.importer.csv.request;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.importer.csv.ProviderRecordImporter;
import org.motechproject.whp.importer.csv.builder.ImportProviderRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
@Ignore("Not required anymore")
public class ProviderRecordImporterIT extends SpringIntegrationTest {
    @Autowired
    AllProviders allProviders;

    @Autowired
    ProviderRecordImporter providerRecordImporter;

    @After
    public void clearDb() {
        if (allProviders.findByProviderId("12") != null)
            markForDeletion(allProviders.findByProviderId("12"));
        if (allProviders.findByProviderId("13") != null)
            markForDeletion(allProviders.findByProviderId("13"));

    }

    @Test
    public void shouldStoreProviders() throws Exception {

        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("12").build();

        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("13").build();

        providerRecordImporter.post(Arrays.asList((Object) importProviderRequest1, importProviderRequest2));

        assertNotNull(allProviders.findByProviderId("12"));
        assertNotNull(allProviders.findByProviderId("13"));
    }
}
