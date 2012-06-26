package org.motechproject.whp.webservice.mapper;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Positive;
import static org.motechproject.whp.refdata.domain.SmearTestSampleInstance.PreTreatment;

public class SmearTestResultsMapperTest {

    SmearTestResultsMapper smearTestResultsMapper = new SmearTestResultsMapper();

    @Test
    public void shouldMapSmearTestInstance() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestInstance("PreTreatment").build();
        SmearTestResults result = smearTestResultsMapper.map(patientWebRequest);
        assertEquals(PreTreatment, result.get(0).getSmear_sample_instance());
    }

    @Test
    public void shouldMapTestDate1() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate1("22/06/2012").build();
        SmearTestResults result = smearTestResultsMapper.map(patientWebRequest);
        assertEquals(new LocalDate(2012, 6, 22), result.get(0).getSmear_test_date_1());
    }

    @Test
    public void shouldMapTestResult1() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestResult1("Positive").build();
        SmearTestResults result = smearTestResultsMapper.map(patientWebRequest);
        assertEquals(Positive, result.get(0).getSmear_test_result_1());
    }

    @Test
    public void shouldMapTestDate2() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate2("22/06/2012").build();
        SmearTestResults result = smearTestResultsMapper.map(patientWebRequest);
        assertEquals(new LocalDate(2012, 6, 22), result.get(0).getSmear_test_date_2());
    }

    @Test
    public void shouldMapTestResult2() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestResult2("Positive").build();
        SmearTestResults result = smearTestResultsMapper.map(patientWebRequest);
        assertEquals(Positive, result.get(0).getSmear_test_result_2());
    }
}
