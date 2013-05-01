package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceSummaryByProviderBuilder {

    private List<String> patientsWithoutAdherence;
    private List<String> patientsWithAdherence;
    private String providerId;

    public AdherenceSummaryByProviderBuilder() {

    }

    public AdherenceSummaryByProvider build(){
        List<PatientAdherenceStatus> patients = new ArrayList<>();
        LocalDate currentAdherenceReportWeekStartDate = currentAdherenceCaptureWeek().startDate();
        for(String patientId : patientsWithAdherence){
            patients.add(new PatientAdherenceStatus(patientId, currentAdherenceReportWeekStartDate));
        }

        for(String patientId : patientsWithoutAdherence){
            patients.add(new PatientAdherenceStatus(patientId, null));
        }

        return new AdherenceSummaryByProvider(providerId, patients);
    }

    public AdherenceSummaryByProviderBuilder withPatientsWithoutAdherence(String... patientsWithoutAdherence) {
        this.patientsWithoutAdherence = asList(patientsWithoutAdherence);
        return this;
    }

    public AdherenceSummaryByProviderBuilder withPatientsWithAdherence(String... patientsWithAdherence) {
        this.patientsWithAdherence = asList(patientsWithAdherence);
        return this;
    }

    public AdherenceSummaryByProviderBuilder withProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }
}
