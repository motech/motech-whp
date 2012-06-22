package org.motechproject.whp.uimodel;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseName;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.WHPConstants;

@Data
public class PatientDTO {

    private String patientId;

    private String ipStartDate;
    private String eipStartDate;
    private String cpStartDate;

    //required by spring
    public PatientDTO() {
    }

    public PatientDTO(Patient patient) {
        Therapy therapy = patient.currentTherapy();
        this.patientId = patient.getPatientId();
        this.ipStartDate = therapy.getPhases().getByPhaseName(PhaseName.IP).getStartDate() != null ? therapy.getPhases().getByPhaseName(PhaseName.IP).getStartDate().toString(WHPConstants.DATE_FORMAT) : "";
        this.eipStartDate = therapy.getPhases().getByPhaseName(PhaseName.EIP).getStartDate() != null ? therapy.getPhases().getByPhaseName(PhaseName.EIP).getStartDate().toString(WHPConstants.DATE_FORMAT) : "";
        this.cpStartDate = therapy.getPhases().getByPhaseName(PhaseName.CP).getStartDate() != null ? therapy.getPhases().getByPhaseName(PhaseName.CP).getStartDate().toString(WHPConstants.DATE_FORMAT) : "";
    }

    public Patient mapNewPhaseInfoToPatient(Patient patient) {
        Phases phases = patient.currentTherapy().getPhases();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(WHPConstants.DATE_FORMAT);
        if (StringUtils.isNotBlank(ipStartDate)) {
            patient.startTherapy(formatter.parseLocalDate(ipStartDate));
        } else {
            patient.startTherapy(null);
        }
        if (StringUtils.isNotBlank(eipStartDate)) {
            phases.setEIPStartDate(formatter.parseLocalDate(eipStartDate));
        } else {
            phases.setEIPStartDate(null);
        }
        if (StringUtils.isNotBlank(cpStartDate)) {
            phases.setCPStartDate(formatter.parseLocalDate(cpStartDate));
        } else {
            phases.setCPStartDate(null);
        }
        return patient;
    }

}
