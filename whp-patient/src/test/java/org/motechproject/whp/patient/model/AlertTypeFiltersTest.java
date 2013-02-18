package org.motechproject.whp.patient.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;
import static org.motechproject.whp.patient.model.AlertTypeFilters.*;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertSeverityParam;

public class AlertTypeFiltersTest {

    AlertTypeFilters alertTypeFilters;

    @Mock
    AlertColorConfiguration alertColorConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setUpDefaultColorConfiguration();
        alertTypeFilters = new AlertTypeFilters(alertColorConfiguration);
    }

    @Test
    public void shouldReturnAlertTypeFilterForAGivenAlertName() {
        assertNull(alertTypeFilters.getFilter(ALL_ALERTS).getAlertType());
        assertNull(alertTypeFilters.getFilter(NO_ALERTS).getAlertType());
        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE).getAlertType(), is(AdherenceMissing));
        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO).getAlertType(), is(AdherenceMissing));
        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE).getAlertType(), is(AdherenceMissing));
        assertThat(alertTypeFilters.getFilter(CUMULATIVE_MISSED_DOSES).getAlertType(), is(CumulativeMissedDoses));
        assertThat(alertTypeFilters.getFilter(TREATMENT_NOT_STARTED).getAlertType(),is(TreatmentNotStarted));
    }

    @Test
    public void shouldReturnAlertTypeColorBasedOnColorConfiguration() {
        assertNull(alertTypeFilters.getFilter(ALL_ALERTS).getColor());
        assertNull(alertTypeFilters.getFilter(NO_ALERTS).getColor());

        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE).getColor(), is("yellow"));
        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO).getColor(), is("red"));
        assertThat(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE).getColor(), is("maroon"));
        assertThat(alertTypeFilters.getFilter(CUMULATIVE_MISSED_DOSES).getColor(), is("blue"));
        assertThat(alertTypeFilters.getFilter(TREATMENT_NOT_STARTED).getColor(),is("brown"));
    }

    @Test
    public void shouldVerifyFilterKeysAndValues() {
        assertFilterKeyAndValue(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE), AdherenceMissing, "1");
        assertFilterKeyAndValue(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO), AdherenceMissing, "2");
        assertFilterKeyAndValue(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE), AdherenceMissing, "3");
        assertFilterKeyAndValue(alertTypeFilters.getFilter(CUMULATIVE_MISSED_DOSES), CumulativeMissedDoses, "1");
        assertFilterKeyAndValue(alertTypeFilters.getFilter(TREATMENT_NOT_STARTED), TreatmentNotStarted, "1");
    }

    private void assertFilterKeyAndValue(AlertTypeFilter filter, PatientAlertType adherenceMissing, String value) {
        assertThat(filter.getFilterKey(), is(alertSeverityParam(adherenceMissing)));
        assertThat(filter.getFilterValue(), is(value));
    }

    private void setUpDefaultColorConfiguration() {
        when(alertColorConfiguration.getColorFor(AdherenceMissing, 1)).thenReturn("yellow");
        when(alertColorConfiguration.getColorFor(AdherenceMissing, 2)).thenReturn("red");
        when(alertColorConfiguration.getColorFor(AdherenceMissing, 3)).thenReturn("maroon");
        when(alertColorConfiguration.getColorFor(CumulativeMissedDoses, 1)).thenReturn("blue");
        when(alertColorConfiguration.getColorFor(TreatmentNotStarted, 1)).thenReturn("brown");
    }
}
