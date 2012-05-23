package org.motechproject.whp.patient.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpdateCommandFactory {

    private Map<String, UpdateCommand> updateCommands = new HashMap<String, UpdateCommand>();

    @Autowired
    public UpdateCommandFactory(List<UpdateCommand> updateCommands) {
        for (UpdateCommand updateCommand : updateCommands) {
            this.updateCommands.put(updateCommand.getCommand(), updateCommand);
        }
    }

    public UpdateCommand updateFor(String commandName) {
        return updateCommands.get(commandName);
    }

}
