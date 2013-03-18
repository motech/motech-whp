package org.motechproject.whp.webservice.mapper;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeightStatisticsMapperTest {

    private WeightStatisticsMapper weightStatisticsMapper;

    @Before
    public void setUp() {
        weightStatisticsMapper = new WeightStatisticsMapper();
    }

    @Test
    public void shouldMapWeightStatisticsDetails() {
        String weight = "99";
        DateTime dateModified = DateTime.now();
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withWeightStatistics(SputumTrackingInstance.PreTreatment.name(), weight).withDate_Modified(dateModified).build();

        WeightStatistics weightStatistics = weightStatisticsMapper.map(patientWebRequest);

        assertThat(weightStatistics.getAll().get(0).getWeight(), is(new Double(weight)));
        assertThat(weightStatistics.getAll().get(0).getWeight_instance(), is(SputumTrackingInstance.PreTreatment));
        assertThat(weightStatistics.getAll().get(0).getMeasuringDate(), is(dateModified.toLocalDate()));
    }

    @Test
    public void shouldNotMapWeightStatisticsDetailsIfItIsEmpty() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().build();

        WeightStatistics weightStatistics = weightStatisticsMapper.map(patientWebRequest);

        assertTrue(weightStatistics.isEmpty());
    }

    @Test
    public void shouldNotMapWeightValueInStatisticsDetailsIfItIsEmpty() {
        DateTime dateModified = DateTime.now();
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withWeightStatistics(SputumTrackingInstance.PreTreatment.name(), null).withDate_Modified(dateModified).build();

        WeightStatistics weightStatistics = weightStatisticsMapper.map(patientWebRequest);

        assertNull(weightStatistics.getAll().get(0).getWeight());
        assertThat(weightStatistics.getAll().get(0).getWeight_instance(), is(SputumTrackingInstance.PreTreatment));
        assertThat(weightStatistics.getAll().get(0).getMeasuringDate(), is(dateModified.toLocalDate()));
    }
}
