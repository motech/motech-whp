package org.motechproject.whp.it.adherence.capture.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.service.ContainerRegistrationValidationPropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ContainerRegistrationValidationPropertiesValuesIT {

    @Autowired
    ContainerRegistrationValidationPropertyValues registrationValidationPropertyValues;

    @Test
    public void shouldGetAllMandatoryFieldsToBeValidated(){
        assertThat(registrationValidationPropertyValues.getMandatoryFields().size(), is(3));
        assertThat(registrationValidationPropertyValues.getMandatoryFields().get(0), is("patientName"));
        assertThat(registrationValidationPropertyValues.getMandatoryFields().get(1), is("age"));
        assertThat(registrationValidationPropertyValues.getMandatoryFields().get(2), is("gender"));
    }

    @Test
    public void shouldReturnWhetherAFieldIsMandatory() {
        assertTrue(registrationValidationPropertyValues.isMandatory("patientName"));
        assertFalse(registrationValidationPropertyValues.isMandatory("patientId"));
    }
}
