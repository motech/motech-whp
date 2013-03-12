package org.motechproject.whp.it.remedi.inbound.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.it.SpringIntegrationTest;
import org.motechproject.whp.it.remedi.inbound.util.CaseXMLBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.mapper.PatientRequestMapper;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.motechproject.whp.webservice.service.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public class PatientRemediAPITest extends SpringIntegrationTest {

    @Autowired
    PatientWebService patientWebService;

    @Autowired
    ProviderService providerService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    RequestValidator validator;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTreatmentCategories allTreatmentCategories;
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRequestMapper patientRequestMapper;
    @Autowired
    private AllDistricts allDistricts;

    District district;
    String defaultProviderId;

    final String PATIENT_ID = "12345";
    final String TREATMENTCATEGORY = "01";
    final String TBID = "tbid";
    final String PROVIDERID = "providerid";
    final DiseaseClass DISEASECLASS = DiseaseClass.P;
    final String AGE = "40";
    final String REGISTRATIONNUMBER = "registrationNumber";


    @Before
    public void setUp() {
        district = new District("district");
        allDistricts.add(district);
    }

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));

    }

    @Test
    public void shouldCreatePatient() throws Exception {
        String createPatientXML = buildXMLWithDefaultData().build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(createPatientXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        assertNotNull(patient);
        assertNotNull(patient.getCurrentTherapy());
    }

    @Test
    public void shouldNotCreatePatientWhenDistrictIsInvalid() throws Exception {
        String createInvalidDistrictXML = buildXMLWithDefaultData().withDistrict("invalid").build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(createInvalidDistrictXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest());

       assertNull(allPatients.findByPatientId(PATIENT_ID));

    }

    @Test
    public void shouldRecordTreatmentsWhenCreatingPatient() throws Exception {

        String patientWithTreatmentXML = buildXMLWithDefaultData().build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(patientWithTreatmentXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient recordedPatient = allPatients.findByPatientId(PATIENT_ID);
        assertNotNull(recordedPatient);

        Therapy currentTherapy = recordedPatient.getCurrentTherapy();
        Treatment currentTreatment = currentTherapy.getCurrentTreatment();

        assertThat(currentTherapy.getTreatmentCategory().getCode(), is(TREATMENTCATEGORY));
        assertThat(currentTreatment.getTbId(), is(TBID));
        assertThat(currentTreatment.getProviderId(), is(PROVIDERID));
        assertThat(currentTherapy.getDiseaseClass(), is(DISEASECLASS));
        assertThat(currentTherapy.getPatientAge(), is(Integer.parseInt(AGE)));
        assertThat(currentTreatment.getTbRegistrationNumber(), is(REGISTRATIONNUMBER));
    }

    private CaseXMLBuilder buildXMLWithDefaultData() {
        return new CaseXMLBuilder("create_patient.xml")
                .withCaseId(PATIENT_ID)
                .withDistrict(district.getName())
                .withTreatmentData(TREATMENTCATEGORY, TBID, PROVIDERID, DISEASECLASS.name(), AGE, REGISTRATIONNUMBER);
    }

    @After
    public void tearDown() {
        allDistricts.remove(district);
        allPatients.removeAll();
        allProviders.removeAll();
    }
}
