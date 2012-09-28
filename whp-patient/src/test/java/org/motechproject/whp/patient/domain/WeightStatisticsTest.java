package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.SampleInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class WeightStatisticsTest {

    @Test
    public void shouldAddANewWeightResultPreservingOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.EndIP, null, DateUtil.today()));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.ExtendedIP, null, DateUtil.today()));

        assertEquals(2, weightStatistics.size());
        assertThat(weightStatistics.get(0).getWeight_instance(), is(SampleInstance.EndIP));
        assertThat(weightStatistics.get(1).getWeight_instance(), is(SampleInstance.ExtendedIP));
    }

    @Test
    public void shouldUpdateCurrentWeightResultAndPushToEndOfList() {
        WeightStatistics weightStatistics = new WeightStatistics();
        LocalDate oldMeasuringDate = DateUtil.today();
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.PreTreatment, null, oldMeasuringDate));
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.ExtendedIP, null, oldMeasuringDate));

        LocalDate newMeasuringDate = new LocalDate(2012, 1, 1);
        WeightStatisticsRecord newWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.ExtendedIP, null, newMeasuringDate);
        weightStatistics.add(newWeightStatisticsRecord);

        assertEquals(2, weightStatistics.size());
        assertThat(weightStatistics.get(0).getWeight_instance(), is(SampleInstance.PreTreatment));
        assertThat(weightStatistics.get(0).getMeasuringDate(), is(oldMeasuringDate));

        assertThat(weightStatistics.get(1).getWeight_instance(), is(SampleInstance.ExtendedIP));
        assertThat(weightStatistics.get(1).getMeasuringDate(), is(newMeasuringDate));
    }

    @Test
    public void shouldReturnLastRecord() {
        WeightStatistics weightStatistics = new WeightStatistics();

        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.PreTreatment, null, new LocalDate(2010, 10, 10)));
        assertThat(weightStatistics.latestResult().getWeight_instance(), is(SampleInstance.PreTreatment));

        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.EndIP, null, new LocalDate(2010, 10, 10)));
        assertThat(weightStatistics.latestResult().getWeight_instance(), is(SampleInstance.EndIP));
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

    @Test
    public void shouldGetPreTreatmentWeightRecord(){
        WeightStatistics weightStatistics = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.PreTreatment, null, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.EndIP, null, DateUtil.today());

        weightStatistics.add(endIpWeightStatisticsRecord);
        weightStatistics.add(pretreatmentWeightStatisticsRecord);

        assertThat(weightStatistics.getPreTreatmentWeightRecord(), is(pretreatmentWeightStatisticsRecord));
    }

    @Test
    public void shouldCheckIfPreTreatmentWeightRecordExists(){
        WeightStatistics weightStatisticsWithPreTreatmentRecord = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.PreTreatment, null, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.EndIP, null, DateUtil.today());

        weightStatisticsWithPreTreatmentRecord.add(endIpWeightStatisticsRecord);
        weightStatisticsWithPreTreatmentRecord.add(pretreatmentWeightStatisticsRecord);

        WeightStatistics weightStatisticsWithoutPreTreatmentRecord = new WeightStatistics();
        weightStatisticsWithoutPreTreatmentRecord.add(endIpWeightStatisticsRecord);

        assertTrue(weightStatisticsWithPreTreatmentRecord.hasPreTreatmentWeightRecord());
        assertFalse(weightStatisticsWithoutPreTreatmentRecord.hasPreTreatmentWeightRecord());
    }
}
