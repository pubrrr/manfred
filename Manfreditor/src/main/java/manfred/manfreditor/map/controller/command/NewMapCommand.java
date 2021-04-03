package manfred.manfreditor.map.controller.command;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.NewMapFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class NewMapCommand implements Command {

    private final MapModel mapModel;
    private final NewMapFactory newMapFactory;
    private final String newMapName;
    private final int columns;
    private final int rows;

    @Override
    public CommandResult execute() {
        Memento<MapModel> backup = mapModel.backup();

        Map newMap = newMapFactory.create(newMapName, PositiveInt.of(columns), PositiveInt.of(rows));
        mapModel.setMap(newMap);
        return CommandResult.successWithRollback(() -> backup.restoreStateOf(mapModel));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapModel mapModel;
        private final NewMapFactory newMapFactory;

        public Command create(String newMapName, int columns, int rows) {
            return new NewMapCommand(mapModel, newMapFactory, newMapName, columns, rows);
        }
    }
}
