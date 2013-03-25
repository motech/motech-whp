package org.motechproject.whp.patientivralert.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PatientAdherenceSummaryMapperTest {

    PatientAdherenceSummaryMapper patientAdherenceSummaryMapper;

    @Before
    public void setUp() {
        patientAdherenceSummaryMapper = new PatientAdherenceSummaryMapper();
    }

    @Test
    public void shouldMapFromReportsContract() {

        PatientAdherenceSummary summary = new PatientAdherenceSummary("patient1", "1234567890", 1);

        List<PatientAdherenceRecord> recordList = patientAdherenceSummaryMapper.map(asList(summary));

        assertThat(recordList.size(), is(1));
        PatientAdherenceRecord record = recordList.get(0);
        assertEquals(summary.getPatientId(), record.getPatientId());
        assertEquals(summary.getMissingWeeks(), record.getMissingWeeks());
        assertEquals(summary.getMobileNumber(), record.getMobileNumber());
    }
}
