package org.motechproject.whp.providerreminder.model;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED;

public class ProviderReminderRequestTest {

    @Test
    public void shouldConvertToXML() throws IOException, JAXBException {
        ProviderReminderRequest request = new ProviderReminderRequest(PROVIDER_ADHERENCE_WINDOW_COMMENCED, asList("msisdn1", "msisdn2"), "requestId");
        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<provider_reminder_request>\n" +
                "    <request_id>requestId</request_id>\n" +
                "    <reminder_type>PROVIDER_ADHERENCE_WINDOW_COMMENCED</reminder_type>\n" +
                "    <msisdns>\n" +
                "        <msisdn>msisdn1</msisdn>\n" +
                "        <msisdn>msisdn2</msisdn>\n" +
                "    </msisdns>\n" +
                "</provider_reminder_request>\n";

        assertEquals(expectedXML, request.toXML());
    }
}
