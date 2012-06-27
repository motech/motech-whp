package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.common.WHPConstants;
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
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder()
                .withSmearTestResults(SmearTestSampleInstance.PreTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SmearTestSampleInstance.EndIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SmearTestSampleInstance.ExtendedIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SmearTestSampleInstance.TwoMonthsIntoCP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SmearTestSampleInstance.EndTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .build();
        SmearTestResults smearTestResults = importSmearTestResultMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, smearTestResults, 0, SmearTestSampleInstance.PreTreatment);
        verifyMapping(importPatientRequest, smearTestResults, 1, SmearTestSampleInstance.EndIP);
        verifyMapping(importPatientRequest, smearTestResults, 2, SmearTestSampleInstance.ExtendedIP);
        verifyMapping(importPatientRequest, smearTestResults, 3, SmearTestSampleInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, smearTestResults, 4, SmearTestSampleInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, SmearTestResults smearTestResults, int smearTestResultIndex, SmearTestSampleInstance type) {
        assertEquals(importPatientRequest.getTestDate1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_1().toString(WHPConstants.DATE_FORMAT));
        assertEquals(importPatientRequest.getTestDate2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_2().toString(WHPConstants.DATE_FORMAT));
        assertEquals(importPatientRequest.getTestResult1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_1().name());
        assertEquals(importPatientRequest.getTestResult2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_2().name());
    }
}
