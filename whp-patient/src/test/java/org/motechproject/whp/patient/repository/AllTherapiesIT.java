package org.motechproject.whp.patient.repository;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllTherapiesIT extends SpringIntegrationTest{
    @Autowired
    private AllTherapies allTherapies;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        allTherapies.removeAll();
    }

    @Test
    public void shouldFetchDateTimeWithTimeZoneSet() {
        Therapy therapy = new Therapy();
        DateTime date = DateTime.parse("20/10/2011 16:30:00", DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss"));
        therapy.setCreationDate(date);
        therapy.setStatus(TherapyStatus.Closed);
        allTherapies.add(therapy);
        Therapy therapyFromDb = allTherapies.get(therapy.getId());
        assertEquals(date, therapyFromDb.getCreationDate());

    }
}
