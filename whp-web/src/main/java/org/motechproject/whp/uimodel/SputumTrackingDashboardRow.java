package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Data
public class SputumTrackingDashboardRow {

    public static final String DIAGNOSIS_POSITIVE = "Positive";
    private String containerId;
    private LocalDate containerIssuedOn;
    private LabResults labResults;
    private LocalDate consultation;
    private String diagnosis;
    private String patientId;
    private String district;
    private String providerId;

    public SputumTrackingDashboardRow(ContainerDashboardRow containerDashboardRow) {
        Container container = containerDashboardRow.getContainer();

        extractContainerInformation(container);
        extractPatientInformation(containerDashboardRow.getPatient(), container.getTbId());
        extractProviderInformation(containerDashboardRow.getProvider());
    }

    private void extractProviderInformation(Provider provider) {
        if (provider != null) {
            providerId = provider.getProviderId();
            district = provider.getDistrict();
        }
    }

    private void extractPatientInformation(Patient patient, String tbId) {
        if (patient != null) {
            patientId = patient.getPatientId();
            consultation = patient.getTreatmentStartDate(tbId);
        }
    }

    private void extractContainerInformation(Container container) {
        containerId = container.getContainerId();
        containerIssuedOn = container.getCreationTime().toLocalDate();
        labResults = container.getLabResults();
        if (isNotBlank(container.getTbId()))
            diagnosis = DIAGNOSIS_POSITIVE;
    }
}
