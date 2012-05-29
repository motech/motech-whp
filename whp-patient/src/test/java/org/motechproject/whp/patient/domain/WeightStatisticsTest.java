package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.WeightInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class WeightStatisticsTest {

    @Test
    public void shouldAddANewWeightResult_ForPreTreatment() {
        WeightStatisticsRecord preTreatmentResults = new WeightStatisticsRecord(WeightInstance.PreTreatment, null, DateUtil.today());

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(preTreatmentResults);

        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.get(0).isOfInstance(WeightInstance.PreTreatment));
    }

    @Test
    public void shouldAddWeightResults_ForBothPreTreatment_AndEndTreatment() {
        WeightStatisticsRecord preTreatmentResults = new WeightStatisticsRecord(WeightInstance.PreTreatment, null, DateUtil.today());
        WeightStatisticsRecord endTreatmentResults = new WeightStatisticsRecord(WeightInstance.EndTreatment, null, DateUtil.today());

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(preTreatmentResults);
        weightStatistics.add(endTreatmentResults);

        assertEquals(2, weightStatistics.size());
        assertTrue(weightStatistics.get(0).isOfInstance(WeightInstance.PreTreatment));
        assertTrue(weightStatistics.get(1).isOfInstance(WeightInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentWeightResult_ForPreTreatment() {
        WeightStatisticsRecord oldPreTreatmentResults = new WeightStatisticsRecord(WeightInstance.PreTreatment, null, new LocalDate(2010, 10, 10));
        WeightStatisticsRecord newPreTreatmentResults = new WeightStatisticsRecord(WeightInstance.PreTreatment, null, new LocalDate(2012, 12, 12));

        WeightStatistics weightStatistics = new WeightStatistics();

        weightStatistics.add(oldPreTreatmentResults);
        assertEquals(1, weightStatistics.size());
        assertEquals(oldPreTreatmentResults, weightStatistics.get(0));

        weightStatistics.add(newPreTreatmentResults);
        assertEquals(1, weightStatistics.size());
        assertEquals(newPreTreatmentResults, weightStatistics.get(0));
    }
}
