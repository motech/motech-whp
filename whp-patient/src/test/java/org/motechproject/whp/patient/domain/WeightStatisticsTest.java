package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.SampleInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class WeightStatisticsTest {

    @Test
    public void shouldAddANewWeightResult_AllInstancesInOrder_shouldPreserveOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (SampleInstance type : SampleInstance.values()) {
            WeightStatisticsRecord weightStatisticsRecord = new WeightStatisticsRecord(type, null, DateUtil.today());
            weightStatistics.add(weightStatisticsRecord);
        }

        assertEquals(5, weightStatistics.size());
        assertTrue(weightStatistics.get(0).isOfInstance(SampleInstance.PreTreatment));
        assertTrue(weightStatistics.get(1).isOfInstance(SampleInstance.EndIP));
        assertTrue(weightStatistics.get(2).isOfInstance(SampleInstance.ExtendedIP));
        assertTrue(weightStatistics.get(3).isOfInstance(SampleInstance.TwoMonthsIntoCP));
        assertTrue(weightStatistics.get(4).isOfInstance(SampleInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentWeightResult_AllInstancesInOrder_shouldPreserveOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (SampleInstance type : SampleInstance.values()) {
            WeightStatisticsRecord oldWeightStatisticsRecord = new WeightStatisticsRecord(type, null, new LocalDate(2010, 10, 10));
            weightStatistics.add(oldWeightStatisticsRecord);
        }

        LocalDate newMeasuringDate = new LocalDate(2010, 12, 12);
        for (SampleInstance type : SampleInstance.values()) {
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
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.PreTreatment, null, new LocalDate(2010, 10, 10)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(SampleInstance.PreTreatment));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.EndIP, null, new LocalDate(2010, 10, 10)));
        assertEquals(2, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(SampleInstance.EndIP));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.ExtendedIP, null, new LocalDate(2010, 10, 10)));
        assertEquals(3, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(SampleInstance.ExtendedIP));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.TwoMonthsIntoCP, null, new LocalDate(2010, 10, 10)));
        assertEquals(4, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(SampleInstance.TwoMonthsIntoCP));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.EndTreatment, null, new LocalDate(2010, 10, 10)));
        assertEquals(5, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(SampleInstance.EndTreatment));
    }

    @Test
    public void shouldReplaceWeightStatisticsAsLatest_WhenStatisticsAreRecentForSameInstance() {
        for (SampleInstance type : SampleInstance.values()) {
            verifyFor(type);
        }
    }

    @Test
    public void shouldReturnWeightStatisticsResultForGivenSampleInstance() {
        WeightStatistics weightStatistics = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.PreTreatment, null, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.EndIP, null, DateUtil.today());

        weightStatistics.add(endIpWeightStatisticsRecord);
        weightStatistics.add(pretreatmentWeightStatisticsRecord);

        assertThat(weightStatistics.resultForInstance(SampleInstance.PreTreatment), is(pretreatmentWeightStatisticsRecord));
        assertThat(weightStatistics.resultForInstance(SampleInstance.EndIP), is(endIpWeightStatisticsRecord));
        assertThat(weightStatistics.resultForInstance(SampleInstance.ExtendedIP), nullValue());
    }

    private void verifyFor(SampleInstance type) {
        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(type, null, new LocalDate(2010, 10, 10)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(type));
        weightStatistics.add(new WeightStatisticsRecord(type, null, new LocalDate(2010, 12, 12)));
        assertEquals(1, weightStatistics.size());
        assertTrue(weightStatistics.latestResult().isOfInstance(type));
    }



}
