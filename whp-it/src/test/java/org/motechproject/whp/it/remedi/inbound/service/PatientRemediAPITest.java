package org.motechproject.whp.it.remedi.inbound.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.WHPDate;
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
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.it.remedi.inbound.util.CaseXMLBuilder.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class PatientRemediAPITest extends SpringIntegrationTest {

    public static final String DEFAULT_CASE_ID = "12341234";
    public static final String DEFAULT_TB_ID = "elevendigit";
    public static final String TREATMENTCATEGORY = "01";
    public static final DiseaseClass DISEASECLASS = DiseaseClass.P;
    public static final String DISTRICT = "district";

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
    private TreatmentCategory treatmentCategory1;

    @Before
    public void setUp() {
        district = new District(DISTRICT);
        allDistricts.add(district);
    }

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        treatmentCategory1 = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek);
        addTreatmentCategory(treatmentCategory1);
    }

    @Test
    public void shouldCreatePatient() throws Exception {

        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDiseaseClass(DISEASECLASS)
                .withTreatmentCategory(TREATMENTCATEGORY).build();
        String createPatientXML = createPatientRequest().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(createPatientXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient patient = allPatients.findByPatientId(DEFAULT_CASE_ID);
        assertNotNull(patient);
        assertNotNull(patient.getCurrentTherapy());

        Therapy currentTherapy = patient.getCurrentTherapy();
        Treatment currentTreatment = currentTherapy.getCurrentTreatment();

        assertEquals(currentTherapy.getDiseaseClass().name(), DISEASECLASS.name());
        assertEquals(currentTherapy.getTreatmentCategory().getCode(), TREATMENTCATEGORY);
        assertEquals(patientWebRequest.getFirst_name(),patient.getFirstName());
        assertEquals(patientWebRequest.getLast_name(),patient.getLastName());
        assertEquals(patientWebRequest.getAge(),currentTherapy.getPatientAge().toString());
        assertEquals(patientWebRequest.getGender(),patient.getGender().name());
        assertEquals(patientWebRequest.getMobile_number(),patient.getPhoneNumber());
        assertEquals(patientWebRequest.getPhi(),patient.getPhi());
        assertEquals(patientWebRequest.getDate_of_birth(),patient.getDateOfBirth().toString(WHPDate.DATE_FORMAT));
        assertEquals(patientWebRequest.getTreatment_category(), patient.getCurrentTherapy().getTreatmentCategory().getCode());

        assertPatientAddress(patientWebRequest, currentTreatment);
        assertTreatment(patientWebRequest, currentTreatment);
        assertLabResults(patientWebRequest, patient);
        assertTreatmentDetails(patientWebRequest, currentTreatment.getTreatmentDetails());
    }

    @Test
    public void shouldValidateForMandatoryTreatmentDetailFieldsForCreatePatientScope() throws Exception {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDiseaseClass(DISEASECLASS)
                .withTreatmentStatus("New")
                .withTreatmentCategory(TREATMENTCATEGORY)
                .withMandatoryTreatmentDetailsAsNull().build();

        String patientWithNullMandatoryFieldsXML = createPatientRequestWithNoMandatoryFields().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(patientWithNullMandatoryFieldsXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        allOf(
                                containsString("hiv_status"),
                                containsString("members_below_six_years"),
                                containsString("provider_name"),
                                containsString("dot_centre"),
                                containsString("provider_type"),
                                containsString("cmf_doctor"),
                                containsString("contact_person_name"),
                                containsString("contact_person_phone_number")
                        )));
    }

    @Test
    public void shouldCreatePatientWithOnlyMandatoryTreatmentDetailFields() throws Exception {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDiseaseClass(DISEASECLASS)
                .withTreatmentStatus("New")
                .withGender(Gender.F.name())
                .withAge("40")
                .withOptionalTreatmentDetailsAsNull()
                .withTreatmentCategory(TREATMENTCATEGORY).build();
        String patientWithMandatoryFiledXML = createPatientRequestWithOnlyMandatoryFields().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(patientWithMandatoryFiledXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient patient = allPatients.findByPatientId(DEFAULT_CASE_ID);
        assertNotNull(patient);
        assertNotNull(patient.getCurrentTherapy());

        Therapy currentTherapy = patient.getCurrentTherapy();
        Treatment currentTreatment = currentTherapy.getCurrentTreatment();

        assertTreatmentDetails(patientWebRequest, currentTreatment.getTreatmentDetails());
    }

    @Test
    public void shouldUpdatePatientWithOnlyMandatoryTreatmentDetailFields() throws Exception {
        createPatientWithDefaults();
        closePatientWithDefaults();

        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDiseaseClass(DISEASECLASS)
                .withTreatmentStatus("New")
                .withOptionalTreatmentDetailsAsNull()
                .withTreatmentCategory(TREATMENTCATEGORY).build();
        String patientWithMandatoryFiledXML = updatePatientRequestWithOnlyMandatoryFields().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(patientWithMandatoryFiledXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient patient = allPatients.findByPatientId(DEFAULT_CASE_ID);
        assertNotNull(patient);
        assertNotNull(patient.getCurrentTherapy());

        Therapy currentTherapy = patient.getCurrentTherapy();
        Treatment currentTreatment = currentTherapy.getCurrentTreatment();

        assertTreatmentDetails(patientWebRequest, currentTreatment.getTreatmentDetails());
    }

    @Test
    public void shouldValidateForMandatoryTreatmentDetailFields() throws Exception {
        createPatientWithDefaults();
        closePatientWithDefaults();

        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDiseaseClass(DISEASECLASS)
                .withTreatmentStatus("New")
                .withTreatmentCategory(TREATMENTCATEGORY)
                .withMandatoryTreatmentDetailsAsNull().build();

        String patientWithNullMandatoryFieldsXML = updatePatientRequestWithNoMandatoryTreatmentDetails().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(patientWithNullMandatoryFieldsXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        allOf(
                                containsString("hiv_status"),
                                containsString("members_below_six_years"),
                                containsString("provider_name"),
                                containsString("dot_centre"),
                                containsString("provider_type"),
                                containsString("cmf_doctor"),
                                containsString("contact_person_name"),
                                containsString("contact_person_phone_number")
                        )));
    }

    private void closePatientWithDefaults() {
        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withCaseId(DEFAULT_CASE_ID)
                .withTbId(DEFAULT_TB_ID)
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);
    }

    private void assertPatientAddress(PatientWebRequest patientWebRequest, Treatment treatment) {
        Address patientAddress = treatment.getPatientAddress();
        assertEquals(patientWebRequest.getAddress_location(), patientAddress.getAddress_location());
        assertEquals(patientWebRequest.getAddress_landmark(), patientAddress.getAddress_landmark());
        assertEquals(patientWebRequest.getAddress_village(), patientAddress.getAddress_village());
        assertEquals(patientWebRequest.getAddress_block(), patientAddress.getAddress_block());
        assertEquals(patientWebRequest.getAddress_district(), patientAddress.getAddress_district());
        assertEquals(patientWebRequest.getAddress_state(), patientAddress.getAddress_state());
    }

    @Test
    public void shouldNotCreatePatientWhenDistrictIsInvalid() throws Exception {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withCaseId(DEFAULT_CASE_ID).withPatientDistrict("invalid").build();
        String createInvalidDistrictXML = createPatientRequest().withRequest(patientWebRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(createInvalidDistrictXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest());

       assertNull(allPatients.findByPatientId(DEFAULT_CASE_ID));

    }

    @Test
    public void shouldUpdatePatient() throws Exception {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDefaultMobileNumber()
                .build();
        patientWebService.createCase(patientWebRequest);

        District new_district = new District("new_district");
        allDistricts.add(new_district);


        String newMobileNumber = "1234567890";
        PatientWebRequest updatedPatientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withCaseId(DEFAULT_CASE_ID)
                .withDefaultMobileNumber()
                .withPatientDistrict(new_district.getName())
                .build();
        String updatePatientXML = new CaseXMLBuilder("patient_simple_update.xml")
                .withRequest(updatedPatientWebRequest)
                .build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(updatePatientXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient updatedPatient = allPatients.findByPatientId(DEFAULT_CASE_ID);
        Therapy currentTherapy = updatedPatient.getCurrentTherapy();
        Treatment currentTreatment = currentTherapy.getCurrentTreatment();
        assertEquals(updatedPatientWebRequest.getAge(), currentTherapy.getPatientAge().toString());
        assertEquals(updatedPatientWebRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatedPatientWebRequest.getTb_id().toLowerCase(), currentTreatment.getTbId());

        assertLabResults(updatedPatientWebRequest, updatedPatient);
        assertPatientAddress(updatedPatientWebRequest, currentTreatment);
        assertThat(updatedPatient.getPhoneNumber(), is(newMobileNumber));
        allDistricts.remove(new_district);
    }

    @Test
    public void shouldTransferInPatient() throws Exception {
        //For the mapping to take place [allTreatmentCategories.findByCode()]
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek);
        addTreatmentCategory(treatmentCategory);

        PatientWebRequest createPatientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(createPatientWebRequest);
        Patient patient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        DateTime dateModified = DateUtil.now();
        DateTime tbRegistrationDate = new DateTime(2012, 11, 11, 0, 0, 0);
        String tbRegistrationNumber = "xxxxxxxxx";

        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);

        PatientWebRequest transferInRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withTbId("tbId")
                .withDate_Modified(dateModified)
                .withTbRegistartionDate(tbRegistrationDate)
                .withTbRegistrationNumber(tbRegistrationNumber)
                .build();
        Provider newProvider = new Provider(transferInRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        String transferInCaseXML = updatePatientRequest().withRequest(transferInRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(transferInCaseXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient updatedPatient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        assertNull(updatedPatient.getTreatmentOutcome());

        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(transferInRequest.getTreatment_category(), updatedPatient.getCurrentTherapy().getTreatmentCategory().getCode());
        assertEquals(patient.getCurrentTherapy().getUid(), updatedPatient.getCurrentTherapy().getUid());

        assertTreatment(transferInRequest, currentTreatment);
        assertLabResults(transferInRequest, updatedPatient);
        assertTreatmentDetails(transferInRequest, currentTreatment.getTreatmentDetails());
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() throws Exception {
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek);
        addTreatmentCategory(treatmentCategory);

        PatientWebRequest createPatientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(createPatientWebRequest);
        Patient patient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        DateTime dateModified = DateUtil.now();
        DateTime tbRegistrationDate = new DateTime(2012, 11, 11, 0, 0, 0);

        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);

        PatientWebRequest newTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForNewTreatment()
                .withDate_Modified(dateModified)
                .withTbRegistartionDate(tbRegistrationDate)
                .build();

        Provider newProvider = new Provider(newTreatmentRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        String transferInCaseXML = updatePatientRequest().withRequest(newTreatmentRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(transferInCaseXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient updatedPatient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        assertNull(updatedPatient.getTreatmentOutcome());

        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(newProvider.getProviderId().toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(newTreatmentRequest.getTreatment_category(), updatedPatient.getCurrentTherapy().getTreatmentCategory().getCode());
        assertNotSame(patient.getCurrentTherapy().getUid(), updatedPatient.getCurrentTherapy().getUid());

        assertLabResults(newTreatmentRequest, updatedPatient);
        assertTreatmentDetails(newTreatmentRequest, currentTreatment.getTreatmentDetails());
    }


    @Test
    public void shouldPauseAndRestartPatientTreatment() throws Exception {
        PatientWebRequest patientWebRequest = createPatientWithDefaults();

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        assertFalse(patient.getCurrentTreatment().isPaused());

        PatientWebRequest pauseTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForPauseTreatment()
                .withTbId(DEFAULT_TB_ID)
                .withCaseId(DEFAULT_CASE_ID)
                .build();

        String pauseTreatmentCaseXML = CaseXMLBuilder.pauseTreatmentRequest().withRequest(pauseTreatmentRequest).build();

        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(pauseTreatmentCaseXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());
        assertTrue(updatedPatient.getCurrentTreatment().isPaused());

        PatientWebRequest restartTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForRestartTreatment()
                .withTbId(DEFAULT_TB_ID)
                .withCaseId(DEFAULT_CASE_ID)
                .build();

        String restartTreatmentCaseXML = CaseXMLBuilder.restartTreatmentRequest().withRequest(restartTreatmentRequest).build();
        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(restartTreatmentCaseXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());
        assertFalse(updatedPatient.getCurrentTreatment().isPaused());
    }


    private PatientWebRequest createPatientWithDefaults() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId(DEFAULT_TB_ID)
                .withCaseId(DEFAULT_CASE_ID)
                .build();

        patientWebService.createCase(patientWebRequest);
        return patientWebRequest;
    }

    @Test
    public void shouldCloseTreatment() throws Exception {
        createPatientWithDefaults();

        // Closing current treatment
        String remarks = "remarks";

        PatientWebRequest closeTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTbId(DEFAULT_TB_ID)
                .withCaseId(DEFAULT_CASE_ID).withTreatmentOutcome(TreatmentOutcome.Cured.name())
                .withRemarks(remarks)
                .build();

        String closeTreatmentCaseXML = CaseXMLBuilder.closeTreatmentRequest().withRequest(closeTreatmentRequest).build();
        standaloneSetup(patientWebService)
                .build()
                .perform(post("/patient/process").body(closeTreatmentCaseXML.getBytes())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Patient updatedPatient = allPatients.findByPatientId(DEFAULT_CASE_ID);
        assertThat(updatedPatient.getCurrentTreatment().getCloseTreatmentRemarks(), is(remarks));
        assertThat(updatedPatient.getCurrentTreatment().getTbId(), is(closeTreatmentRequest.getTb_id()));
        assertThat(updatedPatient.getCurrentTreatment().getTreatmentOutcome(), is(TreatmentOutcome.Cured));
    }

    private void assertTreatment(PatientWebRequest transferInRequest, Treatment currentTreatment) {
        assertEquals(transferInRequest.getTb_id().toLowerCase(), currentTreatment.getTbId());
        assertEquals(transferInRequest.getTb_registration_number(), currentTreatment.getTbRegistrationNumber());
        assertEquals(transferInRequest.getTb_registration_date(), currentTreatment.getStartDate().toString(WHPDate.DATE_FORMAT));
        assertEquals(transferInRequest.getPatient_type(), currentTreatment.getPatientType().name());
        assertEquals(transferInRequest.getProvider_id().toLowerCase(), currentTreatment.getProviderId());
    }

    private void assertLabResults(PatientWebRequest patientWebRequest, Patient patient) {
        SmearTestRecord latestLabResult = patient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(patientWebRequest.getLab_name(), latestLabResult.getLabName());
        assertEquals(patientWebRequest.getLab_number(), latestLabResult.getLabNumber());
        assertEquals(patientWebRequest.getSmear_test_date_1(), latestLabResult.getSmear_test_date_1().toString(WHPDate.DATE_FORMAT));
        assertEquals(patientWebRequest.getSmear_test_date_2(), latestLabResult.getSmear_test_date_2().toString(WHPDate.DATE_FORMAT));
        assertEquals(patientWebRequest.getSmear_test_result_1(), latestLabResult.getSmear_test_result_1().name());
        assertEquals(patientWebRequest.getSmear_test_result_2(), latestLabResult.getSmear_test_result_2().name());

        WeightStatisticsRecord latestWeightRecord = patient.getCurrentTreatment().getWeightStatistics().latestResult();
        assertEquals(patientWebRequest.getWeight(), latestWeightRecord.getWeight().toString());
        assertEquals(patientWebRequest.getWeight_instance(), latestWeightRecord.getWeight_instance().name());
    }

    public static void assertTreatmentDetails(PatientWebRequest patientRequest, TreatmentDetails treatmentDetails){
        assertEquals(patientRequest.getDistrict_with_code(), treatmentDetails.getDistrictWithCode());
        assertEquals(patientRequest.getTb_unit_with_code(), treatmentDetails.getTbUnitWithCode());
        assertEquals(patientRequest.getEp_site(), treatmentDetails.getEpSite());
        assertEquals(patientRequest.getOther_investigations(), treatmentDetails.getOtherInvestigations());
        assertEquals(patientRequest.getPrevious_treatment_history(), treatmentDetails.getPreviousTreatmentHistory());
        assertEquals(patientRequest.getHiv_status(), treatmentDetails.getHivStatus());

        if(patientRequest.getHiv_test_date() != null){
            assertEquals(patientRequest.getHiv_test_date(), treatmentDetails.getHivTestDate().toString(WHPDate.DATE_FORMAT));
        } else {
            assertNull(treatmentDetails.getHivTestDate());
        }

        assertEquals(Integer.valueOf(patientRequest.getMembers_below_six_years()), treatmentDetails.getMembersBelowSixYears());
        assertEquals(patientRequest.getPhc_referred() , treatmentDetails.getPhcReferred());
        assertEquals(patientRequest.getProvider_name(), treatmentDetails.getProviderName());
        assertEquals(patientRequest.getDot_centre(), treatmentDetails.getDotCentre());
        assertEquals(patientRequest.getProvider_type(), treatmentDetails.getProviderType());
        assertEquals(patientRequest.getCmf_doctor(), treatmentDetails.getCmfDoctor());
        assertEquals(patientRequest.getContact_person_name(), treatmentDetails.getContactPersonName());
        assertEquals(patientRequest.getContact_person_phone_number() , treatmentDetails.getContactPersonPhoneNumber());
        assertEquals(patientRequest.getXpert_test_result() , treatmentDetails.getXpertTestResult());
        assertEquals(patientRequest.getXpert_device_number() , treatmentDetails.getXpertDeviceNumber());

        if(patientRequest.getXpert_test_date() != null)
            assertEquals(patientRequest.getXpert_test_date() , treatmentDetails.getXpertTestDate().toString(WHPDate.DATE_FORMAT));
        else
            assertNull(treatmentDetails.getXpertTestDate());

        assertEquals(patientRequest.getRif_resistance_result(), treatmentDetails.getRifResistanceResult());
    }

    private void addTreatmentCategory(TreatmentCategory treatmentCategory) {
        allTreatmentCategories.add(treatmentCategory);
    }

    @After
    public void tearDown() {
        allDistricts.remove(district);
        allPatients.removeAll();
        allProviders.removeAll();
    }
}
