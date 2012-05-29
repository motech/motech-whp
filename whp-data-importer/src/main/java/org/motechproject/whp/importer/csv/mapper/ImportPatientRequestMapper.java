package org.motechproject.whp.importer.csv.mapper;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportPatientRequestMapper {

    private DozerBeanMapper importPatientRequestDozerMapper;
    private ImportWeightStatisticsMapper importWeightStatisticsMapper;
    private ImportSmearTestResultsMapper importSmearTestResultsMapper;

    @Autowired
    public ImportPatientRequestMapper(DozerBeanMapper importPatientRequestDozerMapper,
                                      ImportWeightStatisticsMapper importWeightStatisticsMapper,
                                      ImportSmearTestResultsMapper importSmearTestResultsMapper) {
        this.importPatientRequestDozerMapper = importPatientRequestDozerMapper;
        this.importWeightStatisticsMapper = importWeightStatisticsMapper;
        this.importSmearTestResultsMapper = importSmearTestResultsMapper;
    }

    public PatientRequest map(ImportPatientRequest importPatientRequest) {
        try {
            PatientRequest request = importPatientRequestDozerMapper.map(importPatientRequest, PatientRequest.class);
            request.setSmearTestResults(importSmearTestResultsMapper.map(importPatientRequest));
            request.setWeightStatistics(importWeightStatisticsMapper.map(importPatientRequest));
            return request;
        } catch (MappingException exception) {
            throw new WHPRuntimeException(WHPErrorCode.FIELD_VALIDATION_FAILED, exception.getMessage());
        }
    }
}
