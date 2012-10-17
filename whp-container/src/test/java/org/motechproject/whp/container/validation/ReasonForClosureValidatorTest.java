package org.motechproject.whp.container.validation;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerClosureRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReasonForClosureValidatorTest {
    ReasonForClosureValidator reasonForClosureValidator;

    @Before
    public void setUp() {
        reasonForClosureValidator = new ReasonForClosureValidator();
    }

    @Test
    public void shouldValidateWhetherReasonForClosureAndContainerIdArePresent() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.id.invalid.error", errors.get(0).getCode());
        assertEquals("container.reason.for.closure.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateArePresentIfReasonForClosureIsTbNegative() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.alternate.diagnosis.invalid.error", errors.get(0).getCode());
        assertEquals("container.consultation.date.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateAreNotPresentIfReasonForClosureIsNotTbNegative() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("2");
        containerClosureRequest.setAlternateDiagnosis("alternate one");
        containerClosureRequest.setConsultationDate("22/11/2012");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.alternate.diagnosis.invalid.error", errors.get(0).getCode());
        assertEquals("container.consultation.date.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsInProperFormat() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");
        containerClosureRequest.setAlternateDiagnosis("123");
        containerClosureRequest.setConsultationDate("11/27/11");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(1, errors.size());
        assertEquals("container.consultation.date.invalid.error", errors.get(0).getCode());
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsAfterToday() {
        ContainerClosureRequest containerClosureRequest = new ContainerClosureRequest();
        containerClosureRequest.setContainerId("12345");
        containerClosureRequest.setReason("1");
        containerClosureRequest.setAlternateDiagnosis("123");
        containerClosureRequest.setConsultationDate(DateUtil.tomorrow().toString("dd/MM/yyyy"));

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);

        assertEquals(1, errors.size());
        assertEquals("container.consultation.date.invalid.error", errors.get(0).getCode());
    }
}
