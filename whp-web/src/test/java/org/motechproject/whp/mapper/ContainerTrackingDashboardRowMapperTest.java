package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.domain.Action;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import static org.joda.time.DateTime.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Negative;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;
import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

public class ContainerTrackingDashboardRowMapperTest {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private DateTime now = DateUtil.now();
    @Mock
    private AllReasonForContainerClosures allReasonForContainerClosures;
    @Mock
    private AllAlternateDiagnosis allAlternateDiagnosis;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldMapTrackingRecordIntoDashboardRow() {
        String containerId = "containerId";
        String reasonForClosure = TB_NEGATIVE_CODE;
        String alternateDiagnosis = "456";
        ContainerStatus containerStatus = Open;
        String labName = "MyOwnLab";
        LabResults labResults = new LabResults();
        labResults.setLabName(labName);
        labResults.setSmearTestResult1(SmearTestResult.Positive);
        labResults.setSmearTestDate1(now.toLocalDate());
        labResults.setSmearTestResult2(SmearTestResult.Negative);
        labResults.setSmearTestDate2(now.toLocalDate().plusDays(1));

        Container container = new Container();
        container.setContainerId(containerId);
        container.setStatus(containerStatus);
        container.setCreationTime(now);
        container.setConsultationDate(now.toLocalDate());
        container.setLabResults(labResults);
        container.setReasonForClosure(reasonForClosure);
        container.setAlternateDiagnosis(alternateDiagnosis);
        container.setDiagnosis(Negative);
        container.setStatus(Open);


        String patientId = "patientid";
        String patientName = "patientName";
        container.setPatientId(patientId);
        container.setPatientName(patientName);

        String providerId = "providerid";
        String district = "district";
        container.setProviderId(providerId);
        container.setDistrict(district);
        when(allReasonForContainerClosures.findByCode(reasonForClosure)).thenReturn(new ReasonForContainerClosure("reason text", reasonForClosure));
        when(allAlternateDiagnosis.findByCode(alternateDiagnosis)).thenReturn(new AlternateDiagnosis("alternate diagnosis text", alternateDiagnosis));

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(container);

        verify(allReasonForContainerClosures).findByCode(reasonForClosure);
        verify(allAlternateDiagnosis).findByCode(alternateDiagnosis);
        assertEquals(containerId, row.getContainerId());
        assertEquals(containerStatus.name(), row.getContainerStatus());
        assertEquals(Negative.name(), row.getDiagnosis());
        assertEquals("reason text (alternate diagnosis text)", row.getReasonForClosure());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getContainerIssuedOn());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getConsultation());
        assertEquals(patientId, row.getPatientId());
        assertEquals(district, row.getDistrict());
        assertEquals(providerId, row.getProviderId());
        assertEquals(labName, row.getLabName());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getConsultationOneDate());
        assertEquals(now.toLocalDate().plusDays(1).toString(DATE_FORMAT), row.getConsultationTwoDate());
        assertEquals(SmearTestResult.Positive.name(), row.getConsultationOneResult());
        assertEquals(SmearTestResult.Negative.name(), row.getConsultationTwoResult());
        assertEquals(Action.Close.name(), row.getAction());
        assertEquals(patientName, row.getPatientName());
    }

    @Test
    public void shouldMakeActionAsNoneAsContainerIsClosedByDefaultSputumMappingRequest() {
        when(allReasonForContainerClosures.findByCode(WHPContainerConstants.CLOSURE_DUE_TO_MAPPING)).thenReturn(new ReasonForContainerClosure("sputum default mapping text", CLOSURE_DUE_TO_MAPPING));
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now());
        containerNotMappedToProvider.setStatus(Closed);
        containerNotMappedToProvider.setReasonForClosure(WHPContainerConstants.CLOSURE_DUE_TO_MAPPING);

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerNotMappedToProvider);

        assertEquals(Action.None.name(), row.getAction());
    }

    @Test
    public void shouldChangeDiagnosisToNegativeWhenReasonForClosureIsTbNegative() {
        when(allReasonForContainerClosures.findByCode(TB_NEGATIVE_CODE)).thenReturn(new ReasonForContainerClosure("some reason", TB_NEGATIVE_CODE));
        when(allAlternateDiagnosis.findByCode("some alternate")).thenReturn(new AlternateDiagnosis("some reason", "123"));
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setStatus(Open);
        containerNotMappedToProvider.setCreationTime(now);
        containerNotMappedToProvider.setReasonForClosure(TB_NEGATIVE_CODE);
        containerNotMappedToProvider.setDiagnosis(Positive);
        containerNotMappedToProvider.setAlternateDiagnosis("some alternate");
        containerNotMappedToProvider.setAlternateDiagnosis("some alternate");
        containerNotMappedToProvider.setStatus(Open);


        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerNotMappedToProvider);

        assertNull(row.getProviderId());
        assertEquals(Negative.name(), row.getDiagnosis());
    }

    @Test
    public void shouldNotHaveProviderInformationWhenContainerNotMappedToProvider() {
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setStatus(Open);
        containerNotMappedToProvider.setCreationTime(now);
        containerNotMappedToProvider.setStatus(Open);

        assertNull(new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerNotMappedToProvider).getProviderId());
    }

    @Test
    public void shouldReturnDiagnosisAsPositiveWhenContainerMappedToPatient() {
        Container containerMappedToPatient = new Container();
        containerMappedToPatient.setStatus(Open);
        containerMappedToPatient.setCreationTime(now);
        containerMappedToPatient.setTbId("tbId");
        containerMappedToPatient.setPatientId("patientId");
        containerMappedToPatient.setStatus(Open);

        assertEquals("Positive", new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerMappedToPatient).getDiagnosis());
    }

    @Test
    public void shouldNotHaveConsultationDateWhenContainerNotMappedToPatient() {
        Container containerNotMappedToPatient = new Container();
        containerNotMappedToPatient.setStatus(Open);
        containerNotMappedToPatient.setCreationTime(now);
        containerNotMappedToPatient.setStatus(Open);

        assertNull(new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerNotMappedToPatient).getConsultation());
    }
}
