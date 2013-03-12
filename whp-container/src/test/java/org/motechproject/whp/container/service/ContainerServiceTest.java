package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.apache.commons.httpclient.util.DateParseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.*;
import org.motechproject.whp.container.InvalidContainerIdException;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.builder.request.ContainerPatientMappingReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.ContainerRegistrationReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.ContainerStatusReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.SputumLabResultsCaptureReportingRequestBuilder;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.*;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.service.RemediService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerPatientMappingReportingRequest;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.reports.contract.ContainerStatusReportingRequest;
import org.motechproject.whp.reports.contract.SputumLabResultsCaptureReportingRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.SmearTestResult.Negative;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.InTreatment;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.PreTreatment;

public class ContainerServiceTest extends BaseUnitTest {

    private ContainerService containerService;

    @Mock
    private AllContainers allContainers;
    @Mock
    private RemediService remediService;
    @Mock
    private AllReasonForContainerClosures allReasonForContainerClosures;
    @Mock
    private AllAlternateDiagnosis allAlternateDiagnosis;
    @Mock
    private ProviderService providerService;
    @Mock
    private ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() {
        initMocks(this);
        containerService = new ContainerService(allContainers, remediService, reportingPublisherService, allReasonForContainerClosures, allAlternateDiagnosis, providerService);
    }

    @Test
    public void shouldRestoreDefaultsUponRegistration() throws IOException, TemplateException {
        when(providerService.findByProviderId("providerId")).thenReturn(new Provider());
        ContainerRegistrationRequest containerRegistrationRequest = new ContainerRegistrationRequest("providerId", "12345", SputumTrackingInstance.PreTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        containerService.registerContainer(containerRegistrationRequest);

        verify(allContainers).findByContainerId(anyString());

        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).add(captor.capture());
        Container container = captor.getValue();

        assertEquals(Open, container.getStatus());
        assertEquals(container.getInstance(), container.getCurrentTrackingInstance());
        assertEquals(Pending, container.getDiagnosis());

        verifyRegistrationReportingEventPublication(containerRegistrationRequest, container);
    }

    @Test
    public void shouldGetAllReasonsForClosureForPreTreatment() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number one", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(PreTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = containerService.getAllReasonsPreTreatmentClosureReasons();

        Assert.assertEquals(3, allClosureReasonsForAdmin.size());
        Assert.assertEquals("reason number one", allClosureReasonsForAdmin.get(0).getName());
        Assert.assertEquals("reason number two", allClosureReasonsForAdmin.get(1).getName());
        Assert.assertEquals("reason number three", allClosureReasonsForAdmin.get(2).getName());
        Assert.assertEquals("0", allClosureReasonsForAdmin.get(0).getCode());
        Assert.assertEquals("2", allClosureReasonsForAdmin.get(1).getCode());
        Assert.assertEquals("3", allClosureReasonsForAdmin.get(2).getCode());
    }

    @Test
    public void shouldGetAllReasonsForInTreatment() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number one", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(InTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = containerService.getAllInTreatmentClosureReasons();

        Assert.assertEquals(2, allClosureReasonsForAdmin.size());
        Assert.assertEquals("reason number one", allClosureReasonsForAdmin.get(0).getName());
        Assert.assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        Assert.assertEquals("0", allClosureReasonsForAdmin.get(0).getCode());
        Assert.assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfContainerIdIsAlreadyRegistered() throws IOException, TemplateException {
        String providerId = "provider_one";
        String containerId = "12345";
        RegistrationInstance instance = RegistrationInstance.InTreatment;

        String district = "district1";
        Provider provider = new Provider(providerId, null, district, null);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);

        when(allContainers.findByContainerId(anyString())).thenReturn(new ContainerBuilder().withDefaults().build());
        ContainerRegistrationRequest containerRegistrationRequest = new ContainerRegistrationRequest(providerId, containerId, instance.getDisplayText(), ChannelId.IVR.name(), "callId");

        expectedException.expect(InvalidContainerIdException.class);

        containerService.registerContainer(containerRegistrationRequest);
    }

    @Test
    public void shouldRegisterAContainer() throws IOException, TemplateException {
        mockCurrentDate(DateUtil.now());
        DateTime creationTime = DateUtil.now();
        LocalDate containerIssuedDate = DateUtil.today();

        String providerId = "provider_one";
        String containerId = "12345";
        RegistrationInstance instance = RegistrationInstance.InTreatment;

        String district = "district1";
        Provider provider = new Provider(providerId, null, district, null);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);

        String patientName = "patientName";
        String patientId = "patientid";
        Integer age = 21;
        Gender gender = Gender.M;
        ContainerRegistrationRequest containerRegistrationRequest = new ContainerRegistrationRequest(providerId, containerId, instance.getDisplayText(), ChannelId.IVR.name(), "callId");
        containerRegistrationRequest.setPatientName(patientName);
        containerRegistrationRequest.setAge(age);
        containerRegistrationRequest.setPatientId(patientId);
        containerRegistrationRequest.setGender(gender);
        containerService.registerContainer(containerRegistrationRequest);

        verify(allContainers).findByContainerId(anyString());
        String expectedContainerId = new ContainerId(providerId, containerId, ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER).value();

        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).add(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(providerId.toLowerCase(), actualContainer.getProviderId());
        assertEquals(expectedContainerId, actualContainer.getContainerId());
        assertEquals(creationTime, actualContainer.getCreationTime());
        assertEquals(containerIssuedDate, actualContainer.getContainerIssuedDate());
        assertEquals(instance, actualContainer.getInstance());
        assertEquals(Diagnosis.Pending, actualContainer.getDiagnosis());
        assertEquals(district, actualContainer.getDistrict());
        assertEquals(patientName, actualContainer.getPatientName());
        assertEquals(patientId, actualContainer.getPatientId());
        assertEquals(age, actualContainer.getAge());
        assertEquals(gender, actualContainer.getGender());

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(expectedContainerId, providerId, instance, creationTime);
        verify(remediService).sendContainerRegistrationResponse(containerRegistrationModel);
        verify(providerService).findByProviderId(providerId);

        verifyRegistrationReportingEventPublication(containerRegistrationRequest, actualContainer);
    }

    @Test
    public void shouldReturnWhetherAContainerAlreadyExistsOrNot() {
        String containerId = "containerId";
        when(allContainers.findByContainerId(containerId)).thenReturn(new Container());

        assertTrue(containerService.exists(containerId));
        assertFalse(containerService.exists("non-existent-containerId"));

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers).findByContainerId("non-existent-containerId");
    }

    @Test
    public void shouldGetContainerByContainerId() {
        ContainerId containerId = new ContainerId("providerId", "12345", ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
        Container expectedContainer = new Container("providerId", containerId, RegistrationInstance.InTreatment, DateUtil.now(), "d1");
        when(allContainers.findByContainerId(containerId.value())).thenReturn(expectedContainer);

        Container container = containerService.getContainer(containerId.value());

        assertThat(container, is(expectedContainer));
        verify(allContainers).findByContainerId(containerId.value());
    }

    @Test
    public void shouldUpdateContainer_forMapping() {
        ContainerId containerId = new ContainerId("providerId", "12345", ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
        ReasonForContainerClosure reasonForContainerClosure = new ReasonForContainerClosure("reason", "code");
        Container container = new Container("providerId", containerId, RegistrationInstance.InTreatment, DateUtil.now(), "d1");
        container.mapWith("patient", "tb", SputumTrackingInstance.EndIP, reasonForContainerClosure, today(), DateTime.now());
        container.setConsultationDate(today());
        containerService.updatePatientMapping(container);

        verify(allContainers).update(container);
        verifyMappingReportingEventPublication(container);
    }

    @Test
    public void shouldUpdateContainer_forLabResultsCapture() {
        ContainerId containerId = new ContainerId("providerId", "12345", ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
        Container container = new Container("providerId", containerId, RegistrationInstance.InTreatment, DateUtil.now(), "d1");
        LabResults labResults = new LabResults();
        labResults.setCumulativeResult(Negative);
        labResults.setLabName("TestlabName");
        labResults.setLabNumber("TestlabNumber");
        labResults.setSmearTestDate1(today());
        labResults.setSmearTestDate2(today().minusDays(1));
        labResults.setSmearTestResult1(Positive);
        labResults.setSmearTestResult2(Negative);
        labResults.setCapturedOn(DateTime.now());
        container.setLabResults(labResults);

        containerService.updateLabResults(container);

        verify(allContainers).update(container);

        verifySputumLabResultsCaptureReportingEventPublication(container);
    }

    @Test
    public void shouldUpdateOnlyReasonForClosure() throws DateParseException {
        String reasonCode = "2";
        String containerId = "12345";
        ContainerClosureRequest closureRequest = new ContainerClosureRequest();
        closureRequest.setReason(reasonCode);
        closureRequest.setContainerId(containerId);

        ReasonForContainerClosure reasonForContainerClosure = new ReasonForContainerClosure("some reason", reasonCode);
        when(allReasonForContainerClosures.findByCode(reasonCode)).thenReturn(reasonForContainerClosure);
        Container container = new Container();
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.closeContainer(closureRequest);

        verify(allContainers).findByContainerId(containerId);
        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).update(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(reasonForContainerClosure.getCode(), actualContainer.getReasonForClosure());
        assertEquals(ContainerStatus.Closed, actualContainer.getStatus());
        assertNull(actualContainer.getAlternateDiagnosis());
        assertNull(actualContainer.getConsultationDate());
        assertNull(actualContainer.getDiagnosis());

        verifyStatusUpdateReportingEventPublication(actualContainer);
    }

    @Test
    public void shouldNotCloseIfContainerIdIsInvalid() throws DateParseException {
        String containerId = "12345";
        ContainerClosureRequest closureRequest = new ContainerClosureRequest();
        closureRequest.setContainerId(containerId);

        when(allContainers.findByContainerId(containerId)).thenReturn(null);

        containerService.closeContainer(closureRequest);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
        verifyZeroInteractions(reportingPublisherService);
    }

    @Test
    public void shouldNotCloseIfContainerIsAlreadyClosed() throws DateParseException {
        String containerId = "12345";
        ContainerClosureRequest closureRequest = new ContainerClosureRequest();
        closureRequest.setContainerId(containerId);
        Container container = new Container();
        container.setStatus(ContainerStatus.Closed);

        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.closeContainer(closureRequest);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
        verifyZeroInteractions(reportingPublisherService);
    }

    @Test
    public void shouldUpdateReasonForClosureAlongWithAlternateDiagnosisAndConsultationDateIfItIsTbNegative() throws DateParseException {
        String reasonCode = "1";
        String alternateDiagnosisCode = "2";
        String containerId = "12345";
        ContainerClosureRequest closureRequest = new ContainerClosureRequest();
        closureRequest.setReason(reasonCode);
        closureRequest.setAlternateDiagnosis(alternateDiagnosisCode);
        closureRequest.setConsultationDate("25/11/2012");
        closureRequest.setContainerId(containerId);

        ReasonForContainerClosure reasonForContainerClosure = mock(ReasonForContainerClosure.class);
        when(reasonForContainerClosure.isTbNegative()).thenReturn(true);
        when(allReasonForContainerClosures.findByCode(reasonCode)).thenReturn(reasonForContainerClosure);
        AlternateDiagnosis alternateDiagnosis = new AlternateDiagnosis();
        when(allAlternateDiagnosis.findByCode(alternateDiagnosisCode)).thenReturn(alternateDiagnosis);
        Container container = new Container();
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.closeContainer(closureRequest);

        verify(allContainers).findByContainerId(containerId);
        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).update(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(reasonForContainerClosure.getCode(), actualContainer.getReasonForClosure());
        assertEquals(alternateDiagnosis.getCode(), actualContainer.getAlternateDiagnosis());
        assertEquals(new LocalDate(2012, 11, 25), actualContainer.getConsultationDate());
        assertEquals(Diagnosis.Negative, actualContainer.getDiagnosis());
        assertEquals(ContainerStatus.Closed, actualContainer.getStatus());
        assertNotNull(actualContainer.getClosureDate());

        verifyStatusUpdateReportingEventPublication(actualContainer);
    }

    @Test
    public void shouldGetAllTheAlternateDiagnosisListsForContainerClosure() {
        ArrayList<AlternateDiagnosis> alternateDiagnosises = new ArrayList<>();
        when(allAlternateDiagnosis.getAll()).thenReturn(alternateDiagnosises);

        List<AlternateDiagnosis> actualDiagnosises = containerService.getAllAlternateDiagnosis();

        Assert.assertEquals(alternateDiagnosises, actualDiagnosises);
        verify(allAlternateDiagnosis).getAll();
    }

    @Test
    public void shouldGetAllPreTreatmentContainerClosureReasonsForAdmin() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = containerService.getAllPreTreatmentClosureReasonsForAdmin();

        Assert.assertEquals(2, allClosureReasonsForAdmin.size());
        Assert.assertEquals("reason number two", allClosureReasonsForAdmin.get(0).getName());
        Assert.assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        Assert.assertEquals("2", allClosureReasonsForAdmin.get(0).getCode());
        Assert.assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetAllInTreatmentContainerClosureReasonsForAdmin() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(InTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = containerService.getAllInTreatmentClosureReasons();

        Assert.assertEquals(2, allClosureReasonsForAdmin.size());
        Assert.assertEquals("reason number two", allClosureReasonsForAdmin.get(0).getName());
        Assert.assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        Assert.assertEquals("2", allClosureReasonsForAdmin.get(0).getCode());
        Assert.assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetContainerClosureReasonForMapping() {
        ReasonForContainerClosure reason = new ReasonForContainerClosure("not for admin", "0");
        when(allReasonForContainerClosures.findByCode(WHPContainerConstants.CLOSURE_DUE_TO_MAPPING)).thenReturn(reason);

        ReasonForContainerClosure closureReasonForMapping = containerService.getClosureReasonForMapping();

        Assert.assertEquals("not for admin", closureReasonForMapping.getName());
        Assert.assertEquals("0", closureReasonForMapping.getCode());
    }

    @Test
    public void shouldOpenContainer() {
        String containerId = "1234";
        Container container = new Container();
        container.setStatus(ContainerStatus.Closed);
        container.setDiagnosis(Diagnosis.Positive);
        container.setAlternateDiagnosis("some alternate");
        container.setReasonForClosure("Some reason for closure");
        container.setClosureDate(DateTime.now());
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).update(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(ContainerStatus.Open, actualContainer.getStatus());
        assertNull(actualContainer.getReasonForClosure());
        assertNull(actualContainer.getAlternateDiagnosis());
        assertNull(actualContainer.getClosureDate());

        verifyStatusUpdateReportingEventPublication(actualContainer);
    }

    @Test
    public void shouldResetDiagnosisAndConsultationDateWhileOpeningContainerIfCurrentDiagnosisIsNegative() {
        String containerId = "1234";
        Container container = new Container();
        container.setStatus(ContainerStatus.Closed);
        container.setDiagnosis(Diagnosis.Negative);
        container.setReasonForClosure("Some reason for closure");
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).update(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(ContainerStatus.Open, actualContainer.getStatus());
        assertEquals(Diagnosis.Pending, actualContainer.getDiagnosis());
        assertNull(actualContainer.getReasonForClosure());
        assertNull(actualContainer.getConsultationDate());
        assertEquals(Open, actualContainer.getStatus());

        verifyStatusUpdateReportingEventPublication(actualContainer);
    }

    @Test
    public void shouldNotOpenContainerIfContainerIsInvalid() {
        String containerId = "1234";
        when(allContainers.findByContainerId(containerId)).thenReturn(null);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
        verifyZeroInteractions(reportingPublisherService);
    }

    @Test
    public void shouldNotOpenContainerIfContainerIsAlreadyOpened() {
        String containerId = "1234";
        Container container = new Container();
        container.setStatus(Open);
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
        verifyZeroInteractions(reportingPublisherService);
    }



    private void verifyMappingReportingEventPublication(Container container) {
        ContainerPatientMappingReportingRequest request = new ContainerPatientMappingReportingRequestBuilder().forContainer(container).build();
        verify(reportingPublisherService).reportContainerPatientMapping(request);
    }

    private void verifySputumLabResultsCaptureReportingEventPublication(Container container) {
        SputumLabResultsCaptureReportingRequest reportingRequest = new SputumLabResultsCaptureReportingRequestBuilder().forContainer(container).build();
        verify(reportingPublisherService).reportLabResultsCapture(reportingRequest);
    }

    private void verifyRegistrationReportingEventPublication(ContainerRegistrationRequest containerRegistrationReportingRequest, Container container) {
        ContainerRegistrationReportingRequest expectedContainerRegistrationRequest = new ContainerRegistrationReportingRequestBuilder().forContainer(container)
                .registeredThrough(containerRegistrationReportingRequest.getChannelId())
                .withCallId(containerRegistrationReportingRequest.getCallId())
                .build();
        verify(reportingPublisherService).reportContainerRegistration(expectedContainerRegistrationRequest);
    }

    private void verifyStatusUpdateReportingEventPublication(Container actualContainer) {
        ContainerStatusReportingRequest expectedReportingRequest = new ContainerStatusReportingRequestBuilder().forContainer(actualContainer).build();
        verify(reportingPublisherService).reportContainerStatusUpdate(expectedReportingRequest);
    }
}
