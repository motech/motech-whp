package org.motechproject.whp.patient.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TreatmentUpdateFactory {

    private Map<String, TreatmentUpdate> updateCommands = new HashMap<String, TreatmentUpdate>();

    @Autowired
    public TreatmentUpdateFactory(List<TreatmentUpdate> updateCommands) {
        for (TreatmentUpdate updateCommand : updateCommands) {
            this.updateCommands.put(updateCommand.getCommand(), updateCommand);
        }
    }

    public TreatmentUpdate updateFor(TreatmentUpdateScenario scenario) {
        return updateCommands.get(scenario.getScope());
    }

}
