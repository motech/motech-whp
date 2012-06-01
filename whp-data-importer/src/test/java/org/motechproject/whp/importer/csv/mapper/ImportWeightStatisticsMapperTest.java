package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WeightInstance;
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
                .withWeightStatistics(WeightInstance.PreTreatment, "22/09/2000", "99.7")
                .withWeightStatistics(WeightInstance.EndIP, "22/09/2000", "99.7")
                .withWeightStatistics(WeightInstance.ExtendedIP, "22/09/2000", "99.7")
                .withWeightStatistics(WeightInstance.TwoMonthsIntoCP, "22/09/2000", "99.7")
                .withWeightStatistics(WeightInstance.EndTreatment, "22/09/2000", "99.7")
                .build();
        WeightStatistics weightStatistics = importWeightStatisticsMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, weightStatistics, 0, WeightInstance.PreTreatment);
        verifyMapping(importPatientRequest, weightStatistics, 1, WeightInstance.EndIP);
        verifyMapping(importPatientRequest, weightStatistics, 2, WeightInstance.ExtendedIP);
        verifyMapping(importPatientRequest, weightStatistics, 3, WeightInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, weightStatistics, 4, WeightInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, WeightStatistics weightStatistics, int index, WeightInstance type) {
        assertEquals(importPatientRequest.getWeight(type), weightStatistics.get(index).getWeight().toString());
        assertEquals(importPatientRequest.getWeightDate(type), weightStatistics.get(index).getMeasuringDate().toString(WHPConstants.DATE_FORMAT));
    }
}
