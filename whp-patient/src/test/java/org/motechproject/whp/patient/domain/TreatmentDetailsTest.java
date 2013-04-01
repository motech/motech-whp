package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TreatmentDetailsTest {

    @Test
    public void shouldGetXpertTestDateInTheDesiredFormat(){
        TreatmentDetails treatmentDetails = new TreatmentDetails();
        treatmentDetails.setXpertTestDate(LocalDate.parse("1986-12-25"));

        assertThat(treatmentDetails.getXpertTestDateInDesiredFormat(), is("25/12/1986"));
    }

    @Test
    public void shouldReturnEmptyStringIfXpertTestDateIsNull(){
        TreatmentDetails treatmentDetails = new TreatmentDetails();

        assertThat(treatmentDetails.getXpertTestDateInDesiredFormat(), is(""));
    }

    @Test
    public void shouldReturnEmptyStringIfHivTestDateIsNull(){
        TreatmentDetails treatmentDetails = new TreatmentDetails();

        assertThat(treatmentDetails.getHivTestDateInDesiredFormat(), is(""));
    }

    @Test
    public void shouldGetHivTestDateInTheDesiredFormat(){
        TreatmentDetails treatmentDetails = new TreatmentDetails();
        treatmentDetails.setHivTestDate(LocalDate.parse("1986-12-25"));

        assertThat(treatmentDetails.getHivTestDateInDesiredFormat(), is("25/12/1986"));
    }
}
