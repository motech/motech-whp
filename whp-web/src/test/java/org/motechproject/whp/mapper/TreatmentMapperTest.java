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
        assertEquals(Integer.parseInt(patientRequest.getAge()), treatment.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), treatment.getCategory().value());
        assertEquals(DateUtil.today(), treatment.getStartDate());

        assertEquals(patientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());

        assertSmearTests(patientRequest, treatment);
        assertWeightStatistics(patientRequest, treatment);
    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        Assert.assertEquals(patientRequest.getSmear_sample_instance_1(), treatment.getSmearTestResults().get(0).getSampleInstance1().name());
        Assert.assertEquals(patientRequest.getSmear_test_result_1(), treatment.getSmearTestResults().get(0).getResult1());
        Assert.assertEquals(patientRequest.getSmear_test_date_1(), treatment.getSmearTestResults().get(0).getTestDate1().toString("dd/MM/YYYY"));
        Assert.assertEquals(patientRequest.getSmear_sample_instance_2(), treatment.getSmearTestResults().get(0).getSampleInstance2().name());
        Assert.assertEquals(patientRequest.getSmear_test_result_2(), treatment.getSmearTestResults().get(0).getResult2());
        Assert.assertEquals(patientRequest.getSmear_test_date_2(), treatment.getSmearTestResults().get(0).getTestDate2().toString("dd/MM/YYYY"));
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        Assert.assertEquals(WeightInstance.valueOf(patientRequest.getWeight_instance()), treatment.getWeightStatisticsList().get(0).getWeightInstance());
        Assert.assertEquals(Float.parseFloat(patientRequest.getWeight()), treatment.getWeightStatisticsList().get(0).getWeight(), 0.0);
        Assert.assertEquals(DateUtil.today(), treatment.getWeightStatisticsList().get(0).getMeasuringDate());
    }


}
