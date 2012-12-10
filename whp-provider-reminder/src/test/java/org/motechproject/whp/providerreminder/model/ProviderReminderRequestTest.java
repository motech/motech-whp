package org.motechproject.whp.providerreminder.model;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderRequestTest {

    @Test
    public void shouldConvertToXML() throws IOException, JAXBException {
       ProviderReminderRequest request = new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING.name(),
               asList("msisdn1", "msisdn2"), "requestId");

        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><provider_reminder_request><msisdns><msisdn>msisdn1</msisdn><msisdn>msisdn2</msisdn></msisdns><reminder_type>ADHERENCE_WINDOW_APPROACHING</reminder_type><request_id>requestId</request_id></provider_reminder_request>";
        assertEquals(expectedXML, request.toXML());
    }
}
