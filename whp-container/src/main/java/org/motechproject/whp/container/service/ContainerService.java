package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.builder.request.ContainerPatientMappingReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.ContainerRegistrationReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.ContainerStatusReportingRequestBuilder;
import org.motechproject.whp.container.builder.request.SputumLabResultsCaptureReportingRequestBuilder;
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
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerPatientMappingReportingRequest;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.reports.contract.ContainerStatusReportingRequest;
import org.motechproject.whp.reports.contract.SputumLabResultsCaptureReportingRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.InTreatment;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.PreTreatment;

@Service
public class ContainerService {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private AllContainers allContainers;
    private RemediService remediService;
    private final AllReasonForContainerClosures allReasonForContainerClosures;
    private final AllAlternateDiagnosis allAlternateDiagnosis;
    private ProviderService providerService;
    private ReportingPublisherService reportingPublisherService;


    @Autowired
    public ContainerService(AllContainers allContainers, RemediService remediService, ReportingPublisherService reportingPublisherService, AllReasonForContainerClosures allReasonForContainerClosures, AllAlternateDiagnosis allAlternateDiagnosis, ProviderService providerService) {
        this.allContainers = allContainers;
        this.remediService = remediService;
        this.reportingPublisherService = reportingPublisherService;
        this.allReasonForContainerClosures = allReasonForContainerClosures;
        this.allAlternateDiagnosis = allAlternateDiagnosis;
        this.providerService = providerService;
    }

    public void registerContainer(ContainerRegistrationRequest registrationRequest) throws IOException, TemplateException {
        RegistrationInstance instance = RegistrationInstance.getInstanceForValue(registrationRequest.getInstance());
        DateTime creationTime = now();

        Provider provider = providerService.findByProviderId(registrationRequest.getProviderId());
        Container container = new Container(registrationRequest.getProviderId().toLowerCase(), registrationRequest.getContainerId(), instance, creationTime, provider.getDistrict());
        container.setStatus(Open);
        container.setCurrentTrackingInstance(instance);
        container.setDiagnosis(Pending);
        allContainers.add(container);

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(container.getContainerId(), container.getProviderId(), container.getInstance(), creationTime);
        remediService.sendContainerRegistrationResponse(containerRegistrationModel);
        publishContainerRegistrationReportingEvent(container, registrationRequest.getChannelId(), registrationRequest.getSubmitterId(), registrationRequest.getSubmitterRole());
    }

    public boolean exists(String containerId) {
        return allContainers.findByContainerId(containerId) != null;
    }

    public Container getContainer(String containerId) {
        return allContainers.findByContainerId(containerId);
    }

    public void updatePatientMapping(Container container) {
        allContainers.update(container);
        publishContainerPatientMappingReportingEvent(container);
    }

    public void updateLabResults(Container container) {
        allContainers.update(container);
        publishLabResultsCaptureReportingEvent(container);
    }

    public void openContainer(String containerId) {
        Container container = allContainers.findByContainerId(containerId);

        if (container == null || container.getStatus() == ContainerStatus.Open)
            return;

        container.setStatus(ContainerStatus.Open);
        container.setClosureDate(null);
        container.setReasonForClosure(null);
        container.setAlternateDiagnosis(null);
        resetContainerDiagnosisData(container);
        allContainers.update(container);
        publishContainerStatusUpdateReportingEvent(container);
    }

    public void closeContainer(ContainerClosureRequest reasonForClosureRequest) {
        Container container = allContainers.findByContainerId(reasonForClosureRequest.getContainerId());

        if (container == null || container.getStatus() == Closed)
            return;

        ReasonForContainerClosure reasonForContainerClosure = allReasonForContainerClosures.findByCode(reasonForClosureRequest.getReason());
        container.setReasonForClosure(reasonForContainerClosure.getCode());
        container.setStatus(Closed);
        container.setClosureDate(DateTime.now());

        if (reasonForContainerClosure.isTbNegative())
            populateTbNegativeDetails(reasonForClosureRequest, container);

        allContainers.update(container);
        publishContainerStatusUpdateReportingEvent(container);
    }

    public List<ReasonForContainerClosure> getAllReasonsPreTreatmentClosureReasons() {
        return allReasonForContainerClosures.withTreatmentPhase(PreTreatment);
    }

    public List<ReasonForContainerClosure> getAllInTreatmentClosureReasons() {
        return allReasonForContainerClosures.withTreatmentPhase(InTreatment);
    }

    public List<ReasonForContainerClosure> getAllPreTreatmentClosureReasonsForAdmin() {
        return allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment);
    }

    public List<ReasonForContainerClosure> getAllInTreatmentClosureReasonsForAdmin() {
        return allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, InTreatment);
    }

    public ReasonForContainerClosure getClosureReasonForMapping() {
        return allReasonForContainerClosures.findByCode(CLOSURE_DUE_TO_MAPPING);
    }

    public List<AlternateDiagnosis> getAllAlternateDiagnosis() {
        return allAlternateDiagnosis.getAll();
    }

    private void resetContainerDiagnosisData(Container container) {
        if (container.getDiagnosis() == Diagnosis.Negative) {
            container.setDiagnosis(Diagnosis.Pending);
            container.setConsultationDate(null);
        }
    }

    private void populateTbNegativeDetails(ContainerClosureRequest reasonForClosureRequest, Container container) {
        AlternateDiagnosis alternateDiagnosis = allAlternateDiagnosis.findByCode(reasonForClosureRequest.getAlternateDiagnosis());
        container.setAlternateDiagnosis(alternateDiagnosis.getCode());
        container.setConsultationDate(parseDate(reasonForClosureRequest.getConsultationDate()));
        container.setDiagnosis(Diagnosis.Negative);
    }


    private LocalDate parseDate(String date) {
        List<String> dateFormats = Arrays.asList(new SimpleDateFormat(DATE_FORMAT).toPattern());
        try {
            return new LocalDate(DateUtil.parseDate(date, dateFormats));
        } catch (DateParseException e) {
            throw new RuntimeException("Date cannot be parsed");
        }
    }

    private void publishContainerRegistrationReportingEvent(Container container, String channelId, String submitterId, String submitterRole) {
        ContainerRegistrationReportingRequest containerRegistrationReportingRequest = new ContainerRegistrationReportingRequestBuilder().forContainer(container).registeredThrough(channelId).withSubmitterId(submitterId).withSubmitterRole(submitterRole).build();
        reportingPublisherService.reportContainerRegistration(containerRegistrationReportingRequest);
    }

    private void publishLabResultsCaptureReportingEvent(Container container) {
        SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest = new SputumLabResultsCaptureReportingRequestBuilder().forContainer(container).build();
        reportingPublisherService.reportLabResultsCapture(labResultsCaptureReportingRequest);
    }

    private void publishContainerStatusUpdateReportingEvent(Container container) {
        ContainerStatusReportingRequest containerStatusReportingRequest = new ContainerStatusReportingRequestBuilder().forContainer(container).build();
        reportingPublisherService.reportContainerStatusUpdate(containerStatusReportingRequest);
    }

    private void publishContainerPatientMappingReportingEvent(Container container) {
        ContainerPatientMappingReportingRequest containerPatientMappingReportingRequest = new ContainerPatientMappingReportingRequestBuilder().forContainer(container).build();
        reportingPublisherService.reportContainerPatientMapping(containerPatientMappingReportingRequest);
    }
}
