package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.apache.commons.httpclient.util.DateParseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.service.RemediService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

    @Before
    public void setUp() {
        initMocks(this);
        containerService = new ContainerService(allContainers, remediService, allReasonForContainerClosures, allAlternateDiagnosis);
    }

    @Test
    public void shouldRegisterAContainer() throws IOException, TemplateException {
        DateTime creationDate = DateUtil.now();
        mockCurrentDate(creationDate);
        String providerId = "provider_one";
        String containerId = "1234567890";
        SputumTrackingInstance instance = SputumTrackingInstance.InTreatment;

        containerService.registerContainer(new ContainerRegistrationRequest(providerId, containerId, instance.getDisplayText()));

        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).add(captor.capture());
        Container actualContainer = captor.getValue();
        assertEquals(providerId.toLowerCase(), actualContainer.getProviderId());
        assertEquals(containerId, actualContainer.getContainerId());
        assertEquals(creationDate, actualContainer.getCreationTime());
        assertEquals(instance, actualContainer.getInstance());

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(containerId, providerId, instance, creationDate);
        verify(remediService).sendContainerRegistrationResponse(containerRegistrationModel);
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
        String containerId = "containerId";
        Container expectedContainer = new Container("providerId", containerId, SputumTrackingInstance.InTreatment, DateUtil.now());
        when(allContainers.findByContainerId(containerId)).thenReturn(expectedContainer);

        Container container = containerService.getContainer(containerId);

        assertThat(container, is(expectedContainer));
        verify(allContainers).findByContainerId(containerId);
    }

    @Test
    public void shouldUpdateContainer() {
        Container container = new Container("providerId", "containerId", SputumTrackingInstance.InTreatment, DateUtil.now());

        containerService.update(container);

        verify(allContainers).update(container);
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
    public void shouldGetAllContainerClosureReasonsForAdmin() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("not for admin", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.getAll()).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = containerService.getAllClosureReasonsForAdmin();

        Assert.assertEquals(2, allClosureReasonsForAdmin.size());
        Assert.assertEquals("reason number two", allClosureReasonsForAdmin.get(0).getName());
        Assert.assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        Assert.assertEquals("2", allClosureReasonsForAdmin.get(0).getCode());
        Assert.assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetContainerClosureReasonForMapping() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("not for admin", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.getAll()).thenReturn(reasonForContainerClosures);

        ReasonForContainerClosure closureReasonForMapping = containerService.getClosureReasonForMapping();

        Assert.assertEquals("not for admin", closureReasonForMapping.getName());
        Assert.assertEquals("0", closureReasonForMapping.getCode());
    }

    @Test
    public void shouldOpenContainer() {
        String containerId = "1234";
        Container container = new Container();
        container.setStatus(ContainerStatus.Closed);
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).update(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(ContainerStatus.Open, actualContainer.getStatus());
    }

    @Test
    public void shouldNotOpenContainerIfContainerIsInvalid() {
        String containerId = "1234";
        when(allContainers.findByContainerId(containerId)).thenReturn(null);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
    }

    @Test
    public void shouldNotOpenContainerIfContainerIsAlreadyOpened() {
        String containerId = "1234";
        Container container = new Container();
        container.setStatus(ContainerStatus.Open);
        when(allContainers.findByContainerId(containerId)).thenReturn(container);

        containerService.openContainer(containerId);

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers, never()).update(any(Container.class));
    }

    @After
    public void verifyNoMoreInteractionsOnMocks() {
        verifyNoMoreInteractions(allContainers);
    }
}
