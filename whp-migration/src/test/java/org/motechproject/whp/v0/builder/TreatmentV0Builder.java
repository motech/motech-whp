package org.motechproject.whp.v0.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.v0.domain.*;

import java.util.Arrays;
import java.util.List;

public class TreatmentV0Builder {

    private TreatmentV0 treatmentV0;

    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    public TreatmentV0Builder() {
        treatmentV0 = new TreatmentV0();
    }

    public TreatmentV0 build() {
        return treatmentV0;
    }

    public TreatmentV0Builder withDefaults() {
        TherapyV0 therapy = new TherapyV0Builder().withDefaults().build();

        LocalDate today = DateUtil.today();
        treatmentV0 = new TreatmentV0();
        treatmentV0.setProviderId("providerId");
        treatmentV0.setTbId("elevenDigit");
        treatmentV0.setStartDate(DateUtil.today().minusDays(10));
        treatmentV0.setEndDate(DateUtil.today());
        treatmentV0.setPatientAddress(defaultAddress());
        treatmentV0.setPatientType(PatientTypeV0.New);
        treatmentV0.setTbRegistrationNumber("tbRegistrationNumber");

        treatmentV0.addSmearTestResult(new SmearTestRecordV0(SmearTestSampleInstanceV0.PreTreatment, today, SmearTestResultV0.Negative, today, SmearTestResultV0.Negative));
        treatmentV0.addWeightStatistics(new WeightStatisticsRecordV0(WeightInstanceV0.PreTreatment, 100.0, today));
        treatmentV0.pause("paws", DateUtil.now().minusDays(9));
        treatmentV0.resume("resume", DateUtil.now().minusDays(8));

        treatmentV0.setTherapy(therapy);
        return this;
    }

    private AddressV0 defaultAddress() {
        return new AddressV0("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public TreatmentV0Builder withTherapy(TherapyV0 therapyV0) {
        treatmentV0.setTherapy(therapyV0);
        return this;
    }


}