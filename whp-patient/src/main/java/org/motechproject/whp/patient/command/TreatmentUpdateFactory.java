package org.motechproject.whp.patient.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TreatmentUpdateFactory {

    private OpenNewTreatment openNewTreatment;
    private CloseCurrentTreatment closeCurrentTreatment;
    private TransferInPatient transferInPatient;
    private PauseTreatment pauseTreatment;
    private RestartTreatment restartTreatment;

    @Autowired
    public TreatmentUpdateFactory(OpenNewTreatment openNewTreatment,
                                  CloseCurrentTreatment closeCurrentTreatment,
                                  TransferInPatient transferInPatient,
                                  PauseTreatment pauseTreatment,
                                  RestartTreatment restartTreatment) {
        this.openNewTreatment = openNewTreatment;
        this.closeCurrentTreatment = closeCurrentTreatment;
        this.transferInPatient = transferInPatient;
        this.pauseTreatment = pauseTreatment;
        this.restartTreatment = restartTreatment;
    }

    public TreatmentUpdate updateFor(TreatmentUpdateScenario scenario) {
        switch (scenario) {
            case New:
                return openNewTreatment;
            case Close:
                return closeCurrentTreatment;
            case TransferIn:
                return transferInPatient;
            case Pause:
                return pauseTreatment;
            case Restart:
                return restartTreatment;
            default:
                return null;
        }
    }
}
