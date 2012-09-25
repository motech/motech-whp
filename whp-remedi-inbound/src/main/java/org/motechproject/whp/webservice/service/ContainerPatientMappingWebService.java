package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.motechproject.whp.common.exception.WHPErrorCode.CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE;

@Controller
@RequestMapping("/containerPatientMapping/**")
public class ContainerPatientMappingWebService extends CaseService<ContainerPatientMappingWebRequest>{

    private ContainerService containerService;
    private PatientService patientService;

    @Autowired
    public ContainerPatientMappingWebService(ContainerService containerService, PatientService patientService) {
        super(ContainerPatientMappingWebRequest.class);
        this.containerService = containerService;
        this.patientService = patientService;
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        if(!containerPatientMappingWebRequest.isWellFormed()) {
            throw new WHPCaseException(new WHPError(CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE));
        }

        if(!containerService.exists(containerPatientMappingWebRequest.getCase_id())){
            throw new WHPCaseException(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID));
        }

        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if(null == patient) {
            throw new WHPCaseException(new WHPError(WHPErrorCode.PATIENT_NOT_FOUND));
        } else if(patient.getCurrentTreatment()==null || !patient.getCurrentTreatment().getTbId().equals(containerPatientMappingWebRequest.getTb_id())){
            throw new WHPCaseException(new WHPError(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
        }




        // if container doesn't have lab results bomb
        // if is Patient.getCurrentTreatment() is closed bomb
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }
}
