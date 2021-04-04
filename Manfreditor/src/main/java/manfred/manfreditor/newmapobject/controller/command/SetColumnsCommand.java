package manfred.manfreditor.newmapobject.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class SetColumnsCommand implements Command {

    private final NewMapObjectModel newMapObjectModel;
    private final int columns;

    @Override
    public CommandResult execute() {
        int previousColumns = this.newMapObjectModel.getColumns();
        this.newMapObjectModel.setColumns(columns);
        return CommandResult.successWithRollback(() -> this.newMapObjectModel.setColumns(previousColumns));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final NewMapObjectModel newMapObjectModel;

        public Command create(int columns) {
            return new SetColumnsCommand(newMapObjectModel, columns);
        }
    }
}
