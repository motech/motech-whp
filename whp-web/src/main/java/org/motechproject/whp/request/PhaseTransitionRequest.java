package org.motechproject.whp.request;

import lombok.Data;
import org.motechproject.whp.refdata.domain.PhaseName;

@Data
public class PhaseTransitionRequest {
    String patientId;
    PhaseName phaseToTransitionTo;
}
