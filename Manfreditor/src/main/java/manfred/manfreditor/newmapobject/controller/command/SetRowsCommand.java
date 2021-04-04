package manfred.manfreditor.newmapobject.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class SetRowsCommand implements Command {

    private final NewMapObjectModel newMapObjectModel;
    private final int rows;

    @Override
    public CommandResult execute() {
        int previousRows = this.newMapObjectModel.getRows();
        this.newMapObjectModel.setRows(this.rows);
        return CommandResult.successWithRollback(() -> this.newMapObjectModel.setRows(previousRows));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final NewMapObjectModel newMapObjectModel;

        public Command create(int rows) {
            return new SetRowsCommand(newMapObjectModel, rows);
        }
    }
}
