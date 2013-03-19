package org.motechproject.whp.container.builder.request;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ContainerRegistrationDetails;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.reports.contract.UserGivenPatientDetails;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ContainerRegistrationReportingRequestBuilderTest {
    private ContainerRegistrationReportingRequestBuilder containerRegistrationReportingRequestBuilder;

    ContainerRegistrationReportingRequest containerRegistrationReportingRequest;

    @Before
    public void setUp() throws Exception {
        containerRegistrationReportingRequestBuilder = new ContainerRegistrationReportingRequestBuilder();
    }

    @Test
    public void shouldSetBasicDetailsToTheContainerRegistrationReportingRequest() {
        String district = "district";
        LocalDate issuedOn = DateUtil.today();
        RegistrationInstance instance = RegistrationInstance.PreTreatment;
        Container container = new ContainerBuilder().withDefaults()
                                       .withDiagnosis(Diagnosis.Pending)
                                       .withInstance(instance)
                                       .withContainerIssuedDate(issuedOn)
                                       .withProviderDistrict(district)
                                       .withStatus(ContainerStatus.Open)
                                       .withContainerRegistrationDetails("patientName", "patientId", 98, Gender.F).build();

        containerRegistrationReportingRequest = containerRegistrationReportingRequestBuilder.forContainer(container).build();

        assertThat(containerRegistrationReportingRequest.getContainerId(), is(container.getContainerId()));
        assertThat(containerRegistrationReportingRequest.getProviderId(), is(container.getProviderId()));
        assertThat(containerRegistrationReportingRequest.getProviderDistrict(), is(container.getDistrict()));
        assertThat(containerRegistrationReportingRequest.getIssuedOn(), is(container.getContainerIssuedDate().toDate()));
        assertThat(containerRegistrationReportingRequest.getInstance(), is(container.getInstance().name()));
        assertThat(containerRegistrationReportingRequest.getStatus(), is(container.getStatus().name()));
        assertThat(containerRegistrationReportingRequest.getDiagnosis(), is(container.getDiagnosis().name()));
        assertuserGivenDetails(container, containerRegistrationReportingRequest);
    }

    @Test
    public void shouldNotSetNullContainerRegistrationDetailsContainerRegistrationReportingRequest() {
        String district = "district";
        LocalDate issuedOn = DateUtil.today();
        RegistrationInstance instance = RegistrationInstance.PreTreatment;
        Container container = new ContainerBuilder().withDefaults()
                .withDiagnosis(Diagnosis.Pending)
                .withInstance(instance)
                .withContainerIssuedDate(issuedOn)
                .withProviderDistrict(district)
                .withStatus(ContainerStatus.Open)
                .withContainerRegistrationDetails("patientName", null, null, null).build();

        containerRegistrationReportingRequest = containerRegistrationReportingRequestBuilder.forContainer(container).build();

        UserGivenPatientDetails userGivenPatientDetails = containerRegistrationReportingRequest.getUserGivenPatientDetails();
        ContainerRegistrationDetails containerRegistrationDetails = container.getContainerRegistrationDetails();
        assertThat(userGivenPatientDetails.getPatientName(), is(containerRegistrationDetails.getPatientName()));
        assertNull(userGivenPatientDetails.getPatientAge());
        assertNull(userGivenPatientDetails.getPatientId());
        assertNull(userGivenPatientDetails.getGender());
    }

    private void assertuserGivenDetails(Container container, ContainerRegistrationReportingRequest containerRegistrationReportingRequest) {
        UserGivenPatientDetails userGivenPatientDetails = containerRegistrationReportingRequest.getUserGivenPatientDetails();
        ContainerRegistrationDetails containerRegistrationDetails = container.getContainerRegistrationDetails();
        assertThat(userGivenPatientDetails.getPatientName(), is(containerRegistrationDetails.getPatientName()));
        assertThat(userGivenPatientDetails.getPatientAge(), is(containerRegistrationDetails.getPatientAge()));
        assertThat(userGivenPatientDetails.getPatientId(), is(containerRegistrationDetails.getPatientId()));
        assertThat(userGivenPatientDetails.getGender(), is(containerRegistrationDetails.getPatientGender().name()));
    }
}
