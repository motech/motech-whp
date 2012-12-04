package org.motechproject.whp.it.importer.csv.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.mapper.ImportWeightStatisticsMapper;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ImportWeightStatisticsMapperIT {

    @Autowired
    private ImportWeightStatisticsMapper importWeightStatisticsMapper;

    @Test
    public void shouldMapImportWeightStatisticsToWeightStatistics() {
        ImportPatientRequest importPatientRequest = new ImportPatientRequestBuilder()
                .withWeightStatistics(SputumTrackingInstance.PreTreatment, "22/09/2000", "99.7")
                .withWeightStatistics(SputumTrackingInstance.EndIP, "22/09/2000", "99.7")
                .withWeightStatistics(SputumTrackingInstance.ExtendedIP, "22/09/2000", "99.7")
                .withWeightStatistics(SputumTrackingInstance.TwoMonthsIntoCP, "22/09/2000", "99.7")
                .withWeightStatistics(SputumTrackingInstance.EndTreatment, "22/09/2000", "99.7")
                .build();
        WeightStatistics weightStatistics = importWeightStatisticsMapper.map(importPatientRequest);

        verifyMapping(importPatientRequest, weightStatistics, 0, SputumTrackingInstance.PreTreatment);
        verifyMapping(importPatientRequest, weightStatistics, 1, SputumTrackingInstance.EndIP);
        verifyMapping(importPatientRequest, weightStatistics, 2, SputumTrackingInstance.ExtendedIP);
        verifyMapping(importPatientRequest, weightStatistics, 3, SputumTrackingInstance.TwoMonthsIntoCP);
        verifyMapping(importPatientRequest, weightStatistics, 4, SputumTrackingInstance.EndTreatment);
    }

    private void verifyMapping(ImportPatientRequest importPatientRequest, WeightStatistics weightStatistics, int index, SputumTrackingInstance type) {
        assertEquals(importPatientRequest.getWeight(type), weightStatistics.get(index).getWeight().toString());
        assertEquals(importPatientRequest.getWeightDate(type), weightStatistics.get(index).getMeasuringDate().toString(DATE_FORMAT));
    }
}
