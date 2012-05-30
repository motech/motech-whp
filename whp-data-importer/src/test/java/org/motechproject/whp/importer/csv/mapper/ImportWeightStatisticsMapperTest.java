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
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder().withDefaults().build();
        WeightStatistics weightStatistics = importWeightStatisticsMapper.map(importPatientRequest);

        assertEquals(importPatientRequest.getWeight(WeightInstance.PreTreatment), weightStatistics.get(0).getWeight().toString());
        assertEquals(importPatientRequest.getWeightDate(WeightInstance.PreTreatment), weightStatistics.get(0).getMeasuringDate().toString(WHPConstants.DATE_FORMAT));
    }
}
