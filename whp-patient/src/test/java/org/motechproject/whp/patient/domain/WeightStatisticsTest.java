package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.WeightInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class WeightStatisticsTest {

    @Test
    public void shouldAddANewWeightResult_AllInstancesInOrder_shouldPreserveOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (WeightInstance type : WeightInstance.values()) {
            WeightStatisticsRecord weightStatisticsRecord = new WeightStatisticsRecord(type, null, DateUtil.today());
            weightStatistics.add(weightStatisticsRecord);
        }

        assertEquals(5, weightStatistics.size());
        assertTrue(weightStatistics.get(0).isOfInstance(WeightInstance.PreTreatment));
        assertTrue(weightStatistics.get(1).isOfInstance(WeightInstance.EndIP));
        assertTrue(weightStatistics.get(2).isOfInstance(WeightInstance.ExtendedIP));
        assertTrue(weightStatistics.get(3).isOfInstance(WeightInstance.TwoMonthsIntoCP));
        assertTrue(weightStatistics.get(4).isOfInstance(WeightInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentWeightResult_AllInstancesInOrder_shouldPreserveOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (WeightInstance type : WeightInstance.values()) {
            WeightStatisticsRecord oldWeightStatisticsRecord = new WeightStatisticsRecord(type, null, new LocalDate(2010, 10, 10));
            weightStatistics.add(oldWeightStatisticsRecord);
        }

        LocalDate newMeasuringDate = new LocalDate(2010, 12, 12);
        for (WeightInstance type : WeightInstance.values()) {
            WeightStatisticsRecord newWeightStatisticsRecord = new WeightStatisticsRecord(type, null, newMeasuringDate);
            weightStatistics.add(newWeightStatisticsRecord);
        }

        assertEquals(5, weightStatistics.size());
        assertTrue(weightStatistics.get(0).getMeasuringDate().equals(newMeasuringDate));
        assertTrue(weightStatistics.get(1).getMeasuringDate().equals(newMeasuringDate));
        assertTrue(weightStatistics.get(2).getMeasuringDate().equals(newMeasuringDate));
        assertTrue(weightStatistics.get(3).getMeasuringDate().equals(newMeasuringDate));
        assertTrue(weightStatistics.get(4).getMeasuringDate().equals(newMeasuringDate));
    }

    @Test
    public void shouldAddWeightStatisticsAsLatest_WhenStatisticsAreForDifferentInstance() {
        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(WeightInstance.PreTreatment, null, new LocalDate(2010, 10, 10)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(WeightInstance.PreTreatment));
        weightStatistics.add(new WeightStatisticsRecord(WeightInstance.EndIP, null, new LocalDate(2010, 10, 10)));
        assertEquals(2, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(WeightInstance.EndIP));
        weightStatistics.add(new WeightStatisticsRecord(WeightInstance.ExtendedIP, null, new LocalDate(2010, 10, 10)));
        assertEquals(3, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(WeightInstance.ExtendedIP));
        weightStatistics.add(new WeightStatisticsRecord(WeightInstance.TwoMonthsIntoCP, null, new LocalDate(2010, 10, 10)));
        assertEquals(4, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(WeightInstance.TwoMonthsIntoCP));
        weightStatistics.add(new WeightStatisticsRecord(WeightInstance.EndTreatment, null, new LocalDate(2010, 10, 10)));
        assertEquals(5, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(WeightInstance.EndTreatment));
    }

    @Test
    public void shouldReplaceWeightStatisticsAsLatest_WhenStatisticsAreRecentForSameInstance() {
        for (WeightInstance type : WeightInstance.values()) {
            verifyFor(type);
        }
    }

    private void verifyFor(WeightInstance type) {
        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(type, null, new LocalDate(2010, 10, 10)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(type));
        weightStatistics.add(new WeightStatisticsRecord(type, null, new LocalDate(2010, 12, 12)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(type));
    }
}
