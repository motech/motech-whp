package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ImportSmearTestResultsMapperTest {

    @Autowired
    private ImportSmearTestResultsMapper importSmearTestResultMapper;

    @Test
    public void shouldMapImportSmearTestResultsToSmearTestResults() {
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder()
                .withSmearTestResults(SampleInstance.PreTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SampleInstance.EndIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SampleInstance.ExtendedIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SampleInstance.TwoMonthsIntoCP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SampleInstance.EndTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .build();
        SmearTestResults smearTestResults = importSmearTestResultMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, smearTestResults, 0, SampleInstance.PreTreatment);
        verifyMapping(importPatientRequest, smearTestResults, 1, SampleInstance.EndIP);
        verifyMapping(importPatientRequest, smearTestResults, 2, SampleInstance.ExtendedIP);
        verifyMapping(importPatientRequest, smearTestResults, 3, SampleInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, smearTestResults, 4, SampleInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, SmearTestResults smearTestResults, int smearTestResultIndex, SampleInstance type) {
        assertEquals(importPatientRequest.getTestDate1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_1().toString(DATE_FORMAT));
        assertEquals(importPatientRequest.getTestDate2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_2().toString(DATE_FORMAT));
        assertEquals(importPatientRequest.getTestResult1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_1().name());
        assertEquals(importPatientRequest.getTestResult2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_2().name());
    }
}
