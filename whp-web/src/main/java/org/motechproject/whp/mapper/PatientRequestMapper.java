package org.motechproject.whp.mapper;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.contract.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientRequestMapper {

    private DozerBeanMapper dozerPatientRequestMapper;
    private WeightStatisticsMapper weightStatisticsMapper;
    private SmearTestResultsMapper smearTestResultsMapper;

    @Autowired
    public PatientRequestMapper(DozerBeanMapper dozerPatientRequestMapper,
                                WeightStatisticsMapper weightStatisticsMapper,
                                SmearTestResultsMapper smearTestResultsMapper) {

        this.dozerPatientRequestMapper = dozerPatientRequestMapper;
        this.weightStatisticsMapper = weightStatisticsMapper;
        this.smearTestResultsMapper = smearTestResultsMapper;
    }

    public PatientRequest map(PatientWebRequest patientWebRequest) {
        try {
            PatientRequest request = dozerPatientRequestMapper.map(patientWebRequest, PatientRequest.class);
            request.setSmearTestResults(smearTestResultsMapper.map(patientWebRequest));
            request.setWeightStatistics(weightStatisticsMapper.map(patientWebRequest));
            return request;
        } catch (MappingException exception) {
            throw new WHPRuntimeException(WHPErrorCode.FIELD_VALIDATION_FAILED, exception.getMessage());
        }
    }
}
