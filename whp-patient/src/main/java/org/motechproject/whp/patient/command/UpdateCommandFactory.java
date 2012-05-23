package org.motechproject.whp.patient.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpdateCommandFactory {

    private Map<UpdateScope, UpdateCommand> updateCommands = new HashMap<UpdateScope, UpdateCommand>();

    @Autowired
    public UpdateCommandFactory(List<UpdateCommand> updateCommands) {
        for (UpdateCommand updateCommand : updateCommands) {
            this.updateCommands.put(updateCommand.getCommand(), updateCommand);
        }
    }

    public UpdateCommand updateFor(UpdateScope scope) {
        return updateCommands.get(scope);
    }

}
