package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;

import javax.xml.bind.annotation.XmlElement;

@EqualsAndHashCode
@Getter
class AdherenceStatus {

    @XmlElement(name = "provider_id")
    private String providerId;
    @XmlElement(name = "patient_remaining_count")
    private Integer patientRemainingCount;
    @XmlElement(name = "patient_given_count")
    private Integer patientGivenCount;
    @XmlElement(name = "patients_remaining")
    private PatientsRemaining patientsRemaining;

    AdherenceStatus() {
    }

    AdherenceStatus(AdherenceSummaryByProvider adherenceSummaryByProvider) {
        this.providerId = adherenceSummaryByProvider.getProviderId();
        this.patientRemainingCount = adherenceSummaryByProvider.countOfPatientsWithoutAdherence();
        this.patientGivenCount = adherenceSummaryByProvider.countOfPatientsWithAdherence();

        if (adherenceSummaryByProvider.hasPatientsWithoutAdherence()) {
            this.patientsRemaining = new PatientsRemaining(adherenceSummaryByProvider.getAllPatientIdsWithoutAdherence());
        }
    }
}
