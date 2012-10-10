package org.motechproject.whp.container.tracking.builder;

import org.joda.time.DateTime;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
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

    public  ContainerTrackingRecordBuilder withSmearTestResult1(SmearTestResult smearTestResult){
        containerBuilder.withSmearTestResult1(smearTestResult);
        return this;
    }

    public  ContainerTrackingRecordBuilder withSmearTestResult2(SmearTestResult smearTestResult){
        containerBuilder.withSmearTestResult2(smearTestResult);
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

    public ContainerTrackingRecord build(){
        containerTrackingRecord.setProvider(providerBuilder.build());
        containerTrackingRecord.setPatient(patientBuilder.build());
        containerTrackingRecord.setContainer(containerBuilder.build());
        return containerTrackingRecord;
    }
}

