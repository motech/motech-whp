package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ImportSmearTestResultsMapperTest {

    @Autowired
    private ImportSmearTestResultsMapper importSmearTestResultMapper;

    @Test
    public void shouldMapImportSmearTestResultsToSmearTestResults() {
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder().
                withSmearTestResults("19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Negative.name()).build();
        SmearTestResults smearTestResults = importSmearTestResultMapper.map(importPatientRequest);

        assertEquals(importPatientRequest.getTestDate1(SmearTestSampleInstance.PreTreatment), smearTestResults.get(0).getSmear_test_date_1().toString(WHPConstants.DATE_FORMAT));
        assertEquals(importPatientRequest.getTestDate2(SmearTestSampleInstance.PreTreatment), smearTestResults.get(0).getSmear_test_date_2().toString(WHPConstants.DATE_FORMAT));
        assertEquals(importPatientRequest.getTestResult1(SmearTestSampleInstance.PreTreatment), smearTestResults.get(0).getSmear_test_result_1().name());
        assertEquals(importPatientRequest.getTestResult2(SmearTestSampleInstance.PreTreatment), smearTestResults.get(0).getSmear_test_result_2().name());
    }
}
