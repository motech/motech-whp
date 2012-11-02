package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
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
                .withSmearTestResults(SputumTrackingInstance.PreTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SputumTrackingInstance.EndIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SputumTrackingInstance.ExtendedIP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SputumTrackingInstance.TwoMonthsIntoCP, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withSmearTestResults(SputumTrackingInstance.EndTreatment, "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .build();
        SmearTestResults smearTestResults = importSmearTestResultMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, smearTestResults, 0, SputumTrackingInstance.PreTreatment);
        verifyMapping(importPatientRequest, smearTestResults, 1, SputumTrackingInstance.EndIP);
        verifyMapping(importPatientRequest, smearTestResults, 2, SputumTrackingInstance.ExtendedIP);
        verifyMapping(importPatientRequest, smearTestResults, 3, SputumTrackingInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, smearTestResults, 4, SputumTrackingInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, SmearTestResults smearTestResults, int smearTestResultIndex, SputumTrackingInstance type) {
        assertEquals(importPatientRequest.getTestDate1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_1().toString(DATE_FORMAT));
        assertEquals(importPatientRequest.getTestDate2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_date_2().toString(DATE_FORMAT));
        assertEquals(importPatientRequest.getTestResult1(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_1().name());
        assertEquals(importPatientRequest.getTestResult2(type), smearTestResults.get(smearTestResultIndex).getSmear_test_result_2().name());
    }
}
