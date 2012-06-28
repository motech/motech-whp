package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class ImportWeightStatisticsMapperTest {

    @Autowired
    private ImportWeightStatisticsMapper importWeightStatisticsMapper;

    @Test
    public void shouldMapImportWeightStatisticsToWeightStatistics() {
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder()
                .withWeightStatistics(SampleInstance.PreTreatment, "22/09/2000", "99.7")
                .withWeightStatistics(SampleInstance.EndIP, "22/09/2000", "99.7")
                .withWeightStatistics(SampleInstance.ExtendedIP, "22/09/2000", "99.7")
                .withWeightStatistics(SampleInstance.TwoMonthsIntoCP, "22/09/2000", "99.7")
                .withWeightStatistics(SampleInstance.EndTreatment, "22/09/2000", "99.7")
                .build();
        WeightStatistics weightStatistics = importWeightStatisticsMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, weightStatistics, 0, SampleInstance.PreTreatment);
        verifyMapping(importPatientRequest, weightStatistics, 1, SampleInstance.EndIP);
        verifyMapping(importPatientRequest, weightStatistics, 2, SampleInstance.ExtendedIP);
        verifyMapping(importPatientRequest, weightStatistics, 3, SampleInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, weightStatistics, 4, SampleInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, WeightStatistics weightStatistics, int index, SampleInstance type) {
        assertEquals(importPatientRequest.getWeight(type), weightStatistics.get(index).getWeight().toString());
        assertEquals(importPatientRequest.getWeightDate(type), weightStatistics.get(index).getMeasuringDate().toString(WHPConstants.DATE_FORMAT));
    }
}
