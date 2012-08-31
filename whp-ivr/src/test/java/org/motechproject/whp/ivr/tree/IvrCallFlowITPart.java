package org.motechproject.whp.ivr.tree;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.util.KooKooIvrResponse;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.mockito.Mockito.reset;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public abstract class IvrCallFlowITPart extends SpringIvrIntegrationTest {

    public static final String PATIENT_ID_1 = "patientid1";
    public static final String PATIENT_ID_2 = "patientid2";
    public static final String PATIENT_ID_3 = "patientid3";
    @Autowired
    AdherenceCaptureTree adherenceCaptureTree;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllPatients allPatients;
    @Autowired
    WHPAdherenceService adherenceService;
    @Autowired
    AllAdherenceLogs allAdherenceLogs;
    @Autowired
    WhpIvrMessage whpIvrMessage;

    Provider provider;

    Patient patient1;
    Patient patient2;
    Patient patient3;

    ReportingPublisherService reportingPublisherService;

    @Before
    public void setup() throws IOException, InterruptedException {
        super.setup();
        adherenceCaptureTree.load();

        LocalDate lastMonday = currentAdherenceCaptureWeek().startDate();
        adjustDateTime(lastMonday);

        reportingPublisherService = (ReportingPublisherService) getBean("reportingPublisherService");
        setUpTestData();
    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
        allAdherenceLogs.removeAll();
        reset(reportingPublisherService);
    }

    KooKooIvrResponse recordConfirmedAdherence(String adherence) {
        sendDtmf(adherence);
        return sendDtmf("1");
    }

    private void setUpTestData() {
        provider = new ProviderBuilder().withDefaults().build();
        String providerId = provider.getProviderId();
        allProviders.add(provider);

        LocalDate treatmentStartDate = DateUtil.today().minusDays(10);
        patient1 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId(PATIENT_ID_1).withProviderId(providerId).build();
        patient2 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId(PATIENT_ID_2).withProviderId(providerId).build();
        patient3 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId(PATIENT_ID_3).withProviderId(providerId).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
    }
}