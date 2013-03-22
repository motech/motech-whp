package org.motechproject.whp.it.provider.reminder;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.ArgumentCaptor;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.providerreminder.ivr.ProviderReminderRequestProperties;
import org.motechproject.whp.providerreminder.service.ReminderEventHandler;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations ="classpath*:/applicationProviderReminderContext.xml")
public class ReminderEventHandlerIT extends BaseUnitTest {

    @Autowired
    AllProviders allProviders;
    @Autowired
    AllPatients allPatients;
    @Autowired
    WHPAdherenceService adherenceService;
    @Autowired
    ReminderEventHandler reminderEventHandler;
    @Autowired
    ProviderReminderRequestProperties providerReminderRequestProperties;

    @ReplaceWithMock
    @Autowired
    HttpClientService httpClientService;

    @ReplaceWithMock
    @Autowired
    UUIDGenerator uuidGenerator;

    @Test
    public void shouldReturnMobileNumbersProvidersWithAdherencePending() throws IOException, SAXException {
        Provider providerWithoutAdherence = createProvider("providerid1", "1234567890");
        Provider providerAdherenceAtStartOfTreatmentWeek = createProvider("providerid2", "1234567891");
        Provider providerWithAdherenceAtEndOfTreatmentWeek = createProvider("providerid3", "1234567893");

        createPatient("patient1", providerWithoutAdherence, today().minusWeeks(2));
        createPatient("patient2", providerAdherenceAtStartOfTreatmentWeek, currentAdherenceCaptureWeek().startDate());
        createPatient("patient3", providerWithAdherenceAtEndOfTreatmentWeek, currentAdherenceCaptureWeek().endDate());

        when(uuidGenerator.uuid()).thenReturn("request-id");

        reminderEventHandler.adherenceNotReportedEvent(new MotechEvent(ADHERENCE_NOT_REPORTED_EVENT_NAME));

        ArgumentCaptor<String> reminderXmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(httpClientService).post(eq(providerReminderRequestProperties.getProviderReminderUrl()), reminderXmlCaptor.capture(), any(HashMap.class));
        String reminderXml = reminderXmlCaptor.getValue();


        String expectedRequestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<provider_reminder_request>\n" +
                "    <msisdns>\n" +
                "        <msisdn>1234567890</msisdn>\n" +
                "    </msisdns>\n" +
                "    <reminder_type>PROVIDER_ADHERENCE_NOT_REPORTED</reminder_type>\n" +
                "    <request_id>request-id</request_id>\n" +
                "</provider_reminder_request>";

        assertXMLEqual(expectedRequestXML, reminderXml);
    }


    private Patient createPatient(String patientId, Provider provider, LocalDate lastAdherenceDate) {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId)
                .withProviderId(provider.getProviderId())
                .withCurrentTreatmentStartDate(new LocalDate().minusDays(14))
                .withAdherenceProvidedForLastWeek(lastAdherenceDate)
                .build();
        allPatients.add(patient);
        return patient;
    }

    private Provider createProvider(String providerId1, String msisdn) {
        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId1).withPrimaryMobileNumber(msisdn).build();
        allProviders.add(provider);
        return provider;
    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
    }
}
