package org.motechproject.whp.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.WeightInstance;
import org.motechproject.whp.request.PatientRequest;

import static junit.framework.Assert.assertEquals;

public class TreatmentMapperTest {

    private TreatmentMapper treatmentMapper;

    @Before
    public void setUp() {
        treatmentMapper = new TreatmentMapper();

    }

    @Test
    public void shouldMapTreatment() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        Treatment treatment = treatmentMapper.map(patientRequest);

        assertTreatment(patientRequest, treatment);
    }


    private void assertTreatment(PatientRequest patientRequest, Treatment treatment) {
        assertEquals(Integer.parseInt(patientRequest.getPatient_age()), treatment.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), treatment.getCategory().value());
        assertEquals(DateUtil.today(), treatment.getStartDate());

        assertEquals(patientRequest.getRegistration_number(), treatment.getRegistrationNumber());
        assertEquals(patientRequest.getRegistration_date(), treatment.getRegistrationDate().toString());

        assertSmearTests(patientRequest, treatment);
        assertWeightStatistics(patientRequest, treatment);
    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        Assert.assertEquals(patientRequest.getSmear_sample_instance_1(), treatment.getSmearTestResults().get(0).getSampleInstance1());
        Assert.assertEquals(patientRequest.getSmear_result_1(), treatment.getSmearTestResults().get(0).getResult1());
        Assert.assertEquals(patientRequest.getSmear_test_date_1(), treatment.getSmearTestResults().get(0).getTestDate1().toString());
        Assert.assertEquals(patientRequest.getSmear_sample_instance_2(), treatment.getSmearTestResults().get(0).getSampleInstance2());
        Assert.assertEquals(patientRequest.getSmear_result_2(), treatment.getSmearTestResults().get(0).getResult2());
        Assert.assertEquals(patientRequest.getSmear_test_date_2(), treatment.getSmearTestResults().get(0).getTestDate2().toString());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        Assert.assertEquals(WeightInstance.valueOf(patientRequest.getPatient_weight_instance()), treatment.getWeightStatisticsList().get(0).getWeightInstance());
        Assert.assertEquals(Float.parseFloat(patientRequest.getPatient_weight()), treatment.getWeightStatisticsList().get(0).getWeight(), 0.0);
        Assert.assertEquals(DateUtil.today(), treatment.getWeightStatisticsList().get(0).getMeasuringDate());
    }


}
