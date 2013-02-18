package org.motechproject.whp.it.remedi.inbound.request.provider;

import org.junit.After;
import org.junit.Before;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.common.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public abstract class BaseProviderIT extends SpringIntegrationTest {

    public static final String VALID_DISTRICT = "district";
    @Autowired
    protected RequestValidator validator;
    @Autowired
    protected AllDistricts allDistricts;

    @Autowired
    AllProviders allProviders;

    @Before
    public void districtSetUp() {
        allDistricts.add(new District(VALID_DISTRICT));
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }

}
