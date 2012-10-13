package org.motechproject.whp.container.tracking.builder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.user.builder.ProviderBuilder;

public class ContainerTrackingRecordBuilder {

    private ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();

    private ContainerBuilder containerBuilder = new ContainerBuilder().withDefaults();
    private ProviderBuilder providerBuilder = new ProviderBuilder().withDefaults();
    private PatientBuilder patientBuilder = new PatientBuilder().withDefaults();

    public  ContainerTrackingRecordBuilder withProviderDistrict(String providerDistrict){
        providerBuilder.withDistrict(providerDistrict);
        return this;
    }

    public  ContainerTrackingRecordBuilder withProviderId(String providerId){
        providerBuilder.withProviderId(providerId);
        containerBuilder.withProviderId(providerId);
        patientBuilder.withProviderId(providerId);
        return this;
    }

    public  ContainerTrackingRecordBuilder withCumulativeResult(SmearTestResult smearTestResult){
        containerBuilder.withCumulativeResult(smearTestResult);
        return this;
    }

    public  ContainerTrackingRecordBuilder withInstance(SputumTrackingInstance instance){
        containerBuilder.withInstance(instance);
        return this;
    }

    public  ContainerTrackingRecordBuilder withStatus(ContainerStatus status){
        containerBuilder.withStatus(status);
        return this;
    }

    public  ContainerTrackingRecordBuilder withContainerIssuedDate(DateTime dateTime){
        containerBuilder.withCreatedDateTime(dateTime);
        return this;
    }

    public ContainerTrackingRecordBuilder withConsultationDate(LocalDate consultationDate) {
        patientBuilder.withCurrentTreatmentStartDate(consultationDate);
        return this;
    }

    public ContainerTrackingRecord build(){
        containerTrackingRecord.setProvider(providerBuilder.build());
        containerTrackingRecord.setContainer(containerBuilder.build());

        if(patientBuilder != null) {
            containerTrackingRecord.setPatient(patientBuilder.build());
        }

        return containerTrackingRecord;
    }

    public ContainerTrackingRecordBuilder withDiagnosis(Diagnosis diagnosis) {
        containerBuilder.withDiagnosis(diagnosis);
        return this;
    }

    public ContainerTrackingRecordBuilder withNoPatientMapping() {
        patientBuilder = null;
        containerBuilder.withPatientId(null);
        containerBuilder.withTbId(null);
        return this;
    }

    public ContainerTrackingRecordBuilder withDefaults() {
        patientBuilder.withDefaults();
        containerBuilder.withDefaults();
        providerBuilder.withDefaults();
        return this;
    }

    public ContainerTrackingRecordBuilder withReasonForClosure(String reasonForClosure) {
        containerBuilder.withReasonForClosure(reasonForClosure);
        return this;
    }
}

