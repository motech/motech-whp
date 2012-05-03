package org.motechproject.whp.mapper;

import org.dozer.DozerBeanMapper;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.request.PatientWebRequest;

public class PatientRequestMapper {

    AllTreatmentCategories allTreatmentCategories;
    DozerBeanMapper mapper;

    public PatientRequestMapper(AllTreatmentCategories allTreatmentCategories, DozerBeanMapper mapper) {
        this.allTreatmentCategories = allTreatmentCategories;
        this.mapper = mapper;
    }

    public PatientRequest map(PatientWebRequest patientWebRequest) {
        return mapper.map(patientWebRequest, PatientRequest.class);
    }
}
