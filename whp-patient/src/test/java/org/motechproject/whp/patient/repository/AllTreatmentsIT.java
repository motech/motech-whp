package org.motechproject.whp.patient.repository;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.refdata.domain.TreatmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllTreatmentsIT extends SpringIntegrationTest{
    @Autowired
    private AllTreatments allTreatments;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        allTreatments.removeAll();
    }

    @Test
    public void shouldFetchDateTimeWithTimeZoneSet() {
        Treatment treatment = new Treatment();
        DateTime date = DateTime.parse("20/10/2011 16:30:00", DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss"));
        treatment.setCreationDate(date);
        treatment.setStatus(TreatmentStatus.Closed);
        allTreatments.add(treatment);
        Treatment treatmentFromDb = allTreatments.get(treatment.getId());
        assertEquals(date,treatmentFromDb.getCreationDate());

    }
}
