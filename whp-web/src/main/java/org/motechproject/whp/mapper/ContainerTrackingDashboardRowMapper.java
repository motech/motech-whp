package org.motechproject.whp.mapper;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Component
public class ContainerTrackingDashboardRowMapper {

    public static final String DIAGNOSIS_POSITIVE = "Positive";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private ContainerTrackingDashboardRow row;
    private AllReasonForContainerClosures allReasonForContainerClosures;
    private AllAlternateDiagnosis allAlternateDiagnosis;

    @Autowired
    public ContainerTrackingDashboardRowMapper(AllReasonForContainerClosures allReasonForContainerClosures, AllAlternateDiagnosis allAlternateDiagnosis) {
        this.allReasonForContainerClosures = allReasonForContainerClosures;
        this.allAlternateDiagnosis = allAlternateDiagnosis;
    }

    public ContainerTrackingDashboardRow mapFrom(Container containerTrackingRecord) {
        row = new ContainerTrackingDashboardRow();
        Container container = containerTrackingRecord;

        extractContainerInformation(container);

        extractPatientInformation(containerTrackingRecord);
        extractProviderInformation(containerTrackingRecord);
        return row;
    }

    private void extractProviderInformation(Container container) {
        if (container != null) {
            row.setProviderId(container.getProviderId());
            row.setDistrict(container.getDistrict());
        }
    }

    private void extractPatientInformation(Container container) {
        if (container != null) {
            row.setPatientId(container.getPatientId());
        }
    }

    private void extractContainerInformation(Container container) {
        row.setContainerId(container.getContainerId());
        row.setContainerIssuedOn(inDesiredFormat(container.getCreationTime().toLocalDate()));
        row.setContainerStatus(container.getStatus().name());

        if(container.getDiagnosis() != null) {
            row.setDiagnosis(container.getDiagnosis().name());
        }

        setReasonForClosure(container);
        populateLabResultsData(container.getLabResults());

        if(container.getConsultationDate() != null)
            row.setConsultation(inDesiredFormat(container.getConsultationDate()));

        if (isNotBlank(container.getTbId()))
            row.setDiagnosis(DIAGNOSIS_POSITIVE);
    }

    private void setReasonForClosure(Container container) {
        if(!StringUtils.isEmpty(container.getReasonForClosure())) {
            ReasonForContainerClosure reasonForContainerClosure = allReasonForContainerClosures.findByCode(container.getReasonForClosure());
            String reason = reasonForContainerClosure.getName();
            if(reasonForContainerClosure.getCode().equals(TB_NEGATIVE_CODE)) {
                reason = String.format("%s (%s)", reason, allAlternateDiagnosis.findByCode(container.getAlternateDiagnosis()).getName());
                row.setDiagnosis(Diagnosis.Negative.name());
            }
            row.setReasonForClosure(reason);
        }
    }

    private void populateLabResultsData(LabResults labResults) {
        if(labResults != null) {
            row.setLabName(labResults.getLabName());
            row.setConsultationOneDate(inDesiredFormat(labResults.getSmearTestDate1()));
            row.setConsultationTwoDate(inDesiredFormat(labResults.getSmearTestDate2()));
            row.setConsultationOneResult(labResults.getSmearTestResult1().name());
            row.setConsultationTwoResult(labResults.getSmearTestResult2().name());
        }
    }

    private String inDesiredFormat(LocalDate date) {
        return date.toString(DATE_FORMAT);
    }
}
