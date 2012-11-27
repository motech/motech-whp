package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class WeightStatisticsTest {

    public static final int SOME_WEIGHT = 80;
    private Double weight;

    @Before
    public void setUp() {
        weight = new Double(SOME_WEIGHT);
    }

    @Test
    public void shouldAddANewWeightResultPreservingOrder() {
        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.EndIP, null, DateUtil.today()));
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.ExtendedIP, null, DateUtil.today()));

        assertEquals(2, weightStatistics.size());
        assertThat(weightStatistics.get(0).getWeight_instance(), is(SputumTrackingInstance.EndIP));
        assertThat(weightStatistics.get(1).getWeight_instance(), is(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void shouldUpdateCurrentWeightResultAndPushToEndOfList() {
        WeightStatistics weightStatistics = new WeightStatistics();
        LocalDate oldMeasuringDate = DateUtil.today();
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, weight, oldMeasuringDate));
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.ExtendedIP, weight, oldMeasuringDate));

        LocalDate newMeasuringDate = new LocalDate(2012, 1, 1);
        WeightStatisticsRecord newWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.ExtendedIP, weight, newMeasuringDate);
        weightStatistics.add(newWeightStatisticsRecord);

        assertEquals(2, weightStatistics.size());
        assertThat(weightStatistics.get(0).getWeight_instance(), is(SputumTrackingInstance.PreTreatment));
        assertThat(weightStatistics.get(0).getMeasuringDate(), is(oldMeasuringDate));

        assertThat(weightStatistics.get(1).getWeight_instance(), is(SputumTrackingInstance.ExtendedIP));
        assertThat(weightStatistics.get(1).getMeasuringDate(), is(newMeasuringDate));
    }

    @Test
    public void shouldReturnLastRecord() {
        WeightStatistics weightStatistics = new WeightStatistics();

        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, weight, new LocalDate(2010, 10, 10)));
        assertThat(weightStatistics.latestResult().getWeight_instance(), is(SputumTrackingInstance.PreTreatment));

        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.EndIP, weight, new LocalDate(2010, 10, 10)));
        assertThat(weightStatistics.latestResult().getWeight_instance(), is(SputumTrackingInstance.EndIP));
    }

    @Test
    public void shouldReturnWeightStatisticsResultForGivenSputumTrackingInstance() {
        WeightStatistics weightStatistics = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, weight, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.EndIP, weight, DateUtil.today());

        weightStatistics.add(endIpWeightStatisticsRecord);
        weightStatistics.add(pretreatmentWeightStatisticsRecord);

        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.PreTreatment), is(pretreatmentWeightStatisticsRecord));
        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.EndIP), is(endIpWeightStatisticsRecord));
        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.ExtendedIP), nullValue());
    }

    @Test
    public void shouldReturnNullForEmptySputumTrackingInstance() {
        WeightStatistics weightStatistics = new WeightStatistics();
        WeightStatisticsRecord emptyWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, null, DateUtil.today());
        WeightStatisticsRecord nonEmptyWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.EndIP, weight, DateUtil.today());

        weightStatistics.add(emptyWeightStatisticsRecord);
        weightStatistics.add(nonEmptyWeightStatisticsRecord);

        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.PreTreatment), nullValue());
        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.EndIP), is(nonEmptyWeightStatisticsRecord));
        assertThat(weightStatistics.resultForInstance(SputumTrackingInstance.ExtendedIP), nullValue());
    }

    @Test
    public void shouldGetPreTreatmentWeightRecord(){
        WeightStatistics weightStatistics = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, weight, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.EndIP, weight, DateUtil.today());

        weightStatistics.add(endIpWeightStatisticsRecord);
        weightStatistics.add(pretreatmentWeightStatisticsRecord);

        assertThat(weightStatistics.getPreTreatmentWeightRecord(), is(pretreatmentWeightStatisticsRecord));
    }

    @Test
    public void shouldCheckIfPreTreatmentWeightRecordExists(){
        WeightStatistics weightStatisticsWithPreTreatmentRecord = new WeightStatistics();
        WeightStatisticsRecord pretreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, weight, DateUtil.today());
        WeightStatisticsRecord endIpWeightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.EndIP, weight, DateUtil.today());

        weightStatisticsWithPreTreatmentRecord.add(endIpWeightStatisticsRecord);
        weightStatisticsWithPreTreatmentRecord.add(pretreatmentWeightStatisticsRecord);

        WeightStatistics weightStatisticsWithoutPreTreatmentRecord = new WeightStatistics();
        weightStatisticsWithoutPreTreatmentRecord.add(endIpWeightStatisticsRecord);

        assertTrue(weightStatisticsWithPreTreatmentRecord.hasPreTreatmentWeightRecord());
        assertFalse(weightStatisticsWithoutPreTreatmentRecord.hasPreTreatmentWeightRecord());
    }
}
