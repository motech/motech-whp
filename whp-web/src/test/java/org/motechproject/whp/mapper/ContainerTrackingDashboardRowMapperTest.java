package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.Diagnosis.Negative;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;
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
        ContainerStatus containerStatus = ContainerStatus.Open;
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

        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        containerTrackingRecord.setContainer(container);

        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();
        containerTrackingRecord.setPatient(patient);

        String providerId = "providerid";
        String district = "district";
        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withDistrict(district).build();
        containerTrackingRecord.setProvider(provider);
        when(allReasonForContainerClosures.findByCode(reasonForClosure)).thenReturn(new ReasonForContainerClosure("reason text", reasonForClosure));
        when(allAlternateDiagnosis.findByCode(alternateDiagnosis)).thenReturn(new AlternateDiagnosis("alternate diagnosis text", alternateDiagnosis));

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(containerTrackingRecord);

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
    }

    @Test
    public void shouldNotChangeDiagnosisWhenReasonForClosureIsNotTbNegative() {
        when(allReasonForContainerClosures.findByCode("some non tb negative code")).thenReturn(new ReasonForContainerClosure("some reason", "some non tb negative code"));
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now);
        containerNotMappedToProvider.setReasonForClosure("some non tb negative code");
        containerNotMappedToProvider.setDiagnosis(Positive);

        ContainerTrackingRecord record = new ContainerTrackingRecord();
        record.setContainer(containerNotMappedToProvider);

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(record);

        assertNull(row.getProviderId());
        assertEquals(Positive.name(), row.getDiagnosis());
    }

    @Test
    public void shouldChangeDiagnosisToNegativeWhenReasonForClosureIsTbNegative() {
        when(allReasonForContainerClosures.findByCode(TB_NEGATIVE_CODE)).thenReturn(new ReasonForContainerClosure("some reason", TB_NEGATIVE_CODE));
        when(allAlternateDiagnosis.findByCode("some alternate")).thenReturn(new AlternateDiagnosis("some reason", "123"));
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now);
        containerNotMappedToProvider.setReasonForClosure(TB_NEGATIVE_CODE);
        containerNotMappedToProvider.setDiagnosis(Positive);
        containerNotMappedToProvider.setAlternateDiagnosis("some alternate");

        ContainerTrackingRecord record = new ContainerTrackingRecord();
        record.setContainer(containerNotMappedToProvider);

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(record);

        assertNull(row.getProviderId());
        assertEquals(Negative.name(), row.getDiagnosis());
    }

    @Test
    public void shouldNotHaveProviderInformationWhenContainerNotMappedToProvider() {
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now);

        ContainerTrackingRecord record = new ContainerTrackingRecord();
        record.setContainer(containerNotMappedToProvider);

        assertNull(new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(record).getProviderId());
    }

    @Test
    public void shouldReturnDiagnosisAsPositiveWhenContainerMappedToPatient() {
        Container containerMappedToPatient = new Container();
        containerMappedToPatient.setCreationTime(now);
        containerMappedToPatient.setTbId("tbId");
        containerMappedToPatient.setPatientId("patientId");

        ContainerTrackingRecord record = new ContainerTrackingRecord();
        record.setContainer(containerMappedToPatient);
        assertEquals("Positive", new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(record).getDiagnosis());
    }

    @Test
    public void shouldNotHaveConsultationDateWhenContainerNotMappedToPatient() {
        Container containerNotMappedToPatient = new Container();
        containerNotMappedToPatient.setCreationTime(now);

        ContainerTrackingRecord record = new ContainerTrackingRecord();
        record.setContainer(containerNotMappedToPatient);
        assertNull(new ContainerTrackingDashboardRowMapper(allReasonForContainerClosures, allAlternateDiagnosis).mapFrom(record).getConsultation());
    }
}
