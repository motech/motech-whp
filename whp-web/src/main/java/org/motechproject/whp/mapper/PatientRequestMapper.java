package org.motechproject.whp.mapper;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientRequestMapper {

    DozerBeanMapper dozerPatientRequestMapper;

    @Autowired
    public PatientRequestMapper(DozerBeanMapper dozerPatientRequestMapper) {
        this.dozerPatientRequestMapper = dozerPatientRequestMapper;
    }

    public PatientRequest map(PatientWebRequest patientWebRequest) {
        try {
            return dozerPatientRequestMapper.map(patientWebRequest, PatientRequest.class);
        } catch (MappingException exception) {
            throw new WHPRuntimeException(WHPErrorCode.FIELD_VALIDATION_FAILED, exception.getMessage());
        }
    }

}
