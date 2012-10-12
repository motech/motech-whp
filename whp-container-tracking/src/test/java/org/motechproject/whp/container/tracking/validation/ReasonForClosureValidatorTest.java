package org.motechproject.whp.container.tracking.validation;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.UpdateReasonForClosureRequest;

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
        UpdateReasonForClosureRequest updateReasonForClosureRequest = new UpdateReasonForClosureRequest();

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(updateReasonForClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.id.invalid.error", errors.get(0).getCode());
        assertEquals("container.reason.for.closure.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateArePresentIfReasonForClosureIsTbNegative() {
        UpdateReasonForClosureRequest updateReasonForClosureRequest = new UpdateReasonForClosureRequest();
        updateReasonForClosureRequest.setContainerId("12345");
        updateReasonForClosureRequest.setReason("1");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(updateReasonForClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.alternate.diagnosis.invalid.error", errors.get(0).getCode());
        assertEquals("container.consultation.date.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherAlternateDiagnosisAndConsultationDateAreNotPresentIfReasonForClosureIsNotTbNegative() {
        UpdateReasonForClosureRequest updateReasonForClosureRequest = new UpdateReasonForClosureRequest();
        updateReasonForClosureRequest.setContainerId("12345");
        updateReasonForClosureRequest.setReason("2");
        updateReasonForClosureRequest.setAlternateDiagnosis("alternate one");
        updateReasonForClosureRequest.setConsultationDate("22/11/2012");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(updateReasonForClosureRequest);

        assertEquals(2, errors.size());
        assertEquals("container.alternate.diagnosis.invalid.error", errors.get(0).getCode());
        assertEquals("container.consultation.date.invalid.error", errors.get(1).getCode());
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsInProperFormat() {
        UpdateReasonForClosureRequest updateReasonForClosureRequest = new UpdateReasonForClosureRequest();
        updateReasonForClosureRequest.setContainerId("12345");
        updateReasonForClosureRequest.setReason("1");
        updateReasonForClosureRequest.setAlternateDiagnosis("123");
        updateReasonForClosureRequest.setConsultationDate("11/27/11");

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(updateReasonForClosureRequest);

        assertEquals(1, errors.size());
        assertEquals("container.consultation.date.invalid.error", errors.get(0).getCode());
    }

    @Test
    public void shouldValidateWhetherConsultationDateIsAfterToday() {
        UpdateReasonForClosureRequest updateReasonForClosureRequest = new UpdateReasonForClosureRequest();
        updateReasonForClosureRequest.setContainerId("12345");
        updateReasonForClosureRequest.setReason("1");
        updateReasonForClosureRequest.setAlternateDiagnosis("123");
        updateReasonForClosureRequest.setConsultationDate(DateUtil.tomorrow().toString("dd/MM/yyyy"));

        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(updateReasonForClosureRequest);

        assertEquals(1, errors.size());
        assertEquals("container.consultation.date.invalid.error", errors.get(0).getCode());
    }
}
