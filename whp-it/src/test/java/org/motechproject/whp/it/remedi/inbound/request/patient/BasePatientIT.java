package org.motechproject.whp.it.remedi.inbound.request.patient;

import org.junit.After;
import org.junit.Before;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public abstract class BasePatientIT extends SpringIntegrationTest {

    @Autowired
    protected RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @Before
    public void setUpDefaultProvider() {
        super.before();
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        String defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    protected void validate(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, UpdateScope.createScope);
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
        super.after();
    }
}
