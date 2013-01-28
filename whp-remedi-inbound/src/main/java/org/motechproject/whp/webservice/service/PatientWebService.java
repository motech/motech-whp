package org.motechproject.whp.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.mapper.PatientRequestMapper;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientWebRequest> {

    PatientService patientService;
    RequestValidator validator;
    PatientRequestMapper patientRequestMapper;
    UpdateCommandFactory updateCommandFactory;

    @Autowired
    public PatientWebService(
            PatientService patientService,
            RequestValidator validator,
            PatientRequestMapper patientRequestMapper,
            UpdateCommandFactory updateCommandFactory) {

        super(PatientWebRequest.class);
        this.patientService = patientService;
        this.validator = validator;
        this.patientRequestMapper = patientRequestMapper;
        this.updateCommandFactory = updateCommandFactory;
    }

    @Override
    public void closeCase(PatientWebRequest patientWebRequest) {
    }

    @Override
    public void updateCase(PatientWebRequest patientWebRequest) {
        try {
            formatEmptySmearTestResults(patientWebRequest);
            UpdateScope updateScope = patientWebRequest.updateScope();
            validator.validate(patientWebRequest, updateScope.name());
            update(patientRequestMapper.map(patientWebRequest));
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        try {
            validator.validate(patientWebRequest, UpdateScope.createScope);
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
            patientService.createPatient(patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }

    private void formatEmptySmearTestResults(PatientWebRequest patientWebRequest) {
        patientWebRequest.setSmear_test_date_1(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_date_1()));
        patientWebRequest.setSmear_test_date_2(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_date_2()));
        patientWebRequest.setSmear_test_result_1(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_result_1()));
        patientWebRequest.setSmear_test_result_2(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_result_2()));
        patientWebRequest.setLab_number(replaceEmptyStringWithNull(patientWebRequest.getLab_number()));
        patientWebRequest.setLab_name(replaceEmptyStringWithNull(patientWebRequest.getLab_name()));
    }

    private String replaceEmptyStringWithNull(String fieldValue) {
        return StringUtils.isEmpty(fieldValue) ? null : fieldValue;
    }

    public void update(PatientRequest patientRequest) {
        UpdateScope updateScope = patientRequest.updateScope(canBeTransferred(patientRequest.getCase_id()));
        validator.validate(patientRequest, updateScope.name());
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public boolean canBeTransferred(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        List<WHPErrorCode> errors = new ArrayList<>();
        if (patient == null) {
            errors.add(WHPErrorCode.INVALID_PATIENT_CASE_ID);
            return false;
        } else if (!patient.hasCurrentTreatment()) {
            errors.add(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
            return false;
        } else {
            return TreatmentOutcome.TransferredOut.equals(patient.getCurrentTreatment().getTreatmentOutcome());
        }
    }

}
