package org.motechproject.whp.container.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.contract.ContainerClosureRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReasonForClosureValidatorTest {
    ReasonForClosureValidator reasonForClosureValidator;

    @Before
    public void setUp() {
        reasonForClosureValidator = new ReasonForClosureValidator();
    }

    @Test
    public void shouldValidateWhetherReasonForClosureAndContainerIdArePresent() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();

        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertTrue(StringUtils.contains(errors.get(0), "Container Id must be of"));
        assertTrue(StringUtils.contains(errors.get(1), "Enter reason for closure"));
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateArePresentIfReasonForClosureIsTbNegative() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");

        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertTrue(StringUtils.contains(errors.get(0), "Enter alternate diagnosis"));
        assertTrue(StringUtils.contains(errors.get(1), "Enter consultation date"));
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateAreNotPresentIfReasonForClosureIsNotTbNegative() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("2");
        containerClosureRequest.setAlternateDiagnosis("alternate one");
        containerClosureRequest.setConsultationDate("22/11/2012");

        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertTrue(StringUtils.contains(errors.get(0), "Enter alternate diagnosis"));
        assertTrue(StringUtils.contains(errors.get(1), "Enter consultation date"));
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsInProperFormat() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");
        containerClosureRequest.setAlternateDiagnosis("123");
        containerClosureRequest.setConsultationDate("11/27/11");

        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(1, errors.size());
        assertTrue(StringUtils.contains(errors.get(0), "Enter consultation date"));
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsAfterToday() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");
        containerClosureRequest.setAlternateDiagnosis("123");
        containerClosureRequest.setConsultationDate(DateUtil.tomorrow().toString("dd/MM/yyyy"));

        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(1, errors.size());
        assertTrue(StringUtils.contains(errors.get(0), "Enter consultation date"));
    }
}
