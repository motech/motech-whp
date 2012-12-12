package org.motechproject.whp.providerreminder.model;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderRequestTest {

    @Test
    public void shouldConvertToXML() throws IOException, JAXBException {
        ProviderReminderRequest request = new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING,
               asList("msisdn1", "msisdn2"), "requestId");

        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<provider_reminder_request>\n" +
                "    <msisdns>\n" +
                "        <msisdn>msisdn1</msisdn>\n" +
                "        <msisdn>msisdn2</msisdn>\n" +
                "    </msisdns>\n" +
                "    <reminder_type>ADHERENCE_WINDOW_APPROACHING</reminder_type>\n" +
                "    <request_id>requestId</request_id>\n" +
                "</provider_reminder_request>\n";

        assertEquals(expectedXML, request.toXML());
    }

    public static class ProviderReminderConfigurationTest {

        @Test
        public void shouldCreateProviderReminderConfigurationFromDate() {
            Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();

            ProviderReminderConfiguration tuesdayReminderConfiguration = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, tuesday);

            assertEquals(ADHERENCE_WINDOW_APPROACHING, tuesdayReminderConfiguration.getReminderType());
            assertEquals(DayOfWeek.Tuesday, tuesdayReminderConfiguration.getDayOfWeek());
            assertEquals(10, tuesdayReminderConfiguration.getHour());
            assertEquals(30, tuesdayReminderConfiguration.getMinute());
        }
    }
}
