package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AdherenceSummaryByProviderBuilder {

    private List<String> patientsWithoutAdherence;
    private List<String> patientsWithAdherence;
    private String providerId;

    public AdherenceSummaryByProviderBuilder() {

    }

    public AdherenceSummaryByProvider build(){
        List<Patient> patients = new ArrayList<>();

        for(String patientId : patientsWithAdherence){
            patients.add(new PatientBuilder().withDefaults().withPatientId(patientId).withTherapyStartDate(new LocalDate(2012,7,7)).withAdherenceProvidedForLastWeek().build());
        }

        for(String patientId : patientsWithoutAdherence){
            patients.add(new PatientBuilder().withDefaults().withPatientId(patientId).build());
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
