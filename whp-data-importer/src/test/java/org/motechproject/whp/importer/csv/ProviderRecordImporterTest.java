package org.motechproject.whp.importer.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportProviderRequestBuilder;
import org.motechproject.whp.importer.csv.mapper.ProviderRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ProviderRecordImporterTest{
    @Autowired
    AllProviders allProviders;

    @Autowired
    ProviderRecordImporter providerRecordImporter;

    @Autowired
    ProviderRequestMapper providerRequestMapper;

    @Before
    @After
    public void clearDb() {
       allProviders.removeAll();
    }

    @Test
    public void shouldStoreProviders() throws Exception {

        ImportProviderRequest importProviderRequest1 = new ImportProviderRequestBuilder().withDefaults("12").build();

        ImportProviderRequest importProviderRequest2 = new ImportProviderRequestBuilder().withDefaults("13").build();

        providerRecordImporter.post(Arrays.asList((Object) importProviderRequest1, importProviderRequest2));

        assertEquals(2,allProviders.getAll().size());
        assertNotNull(allProviders.findByProviderId("12"));
        assertNotNull(allProviders.findByProviderId("13"));
    }
}
