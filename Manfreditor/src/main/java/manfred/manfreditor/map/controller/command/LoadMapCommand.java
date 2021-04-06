package manfred.manfreditor.map.controller.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.MapRepository;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadMapCommand implements Command {

    private final MapModel mapModel;
    private final MapRepository mapRepository;
    private final MapRepository.MapKey mapKey;

    @Override
    public CommandResult execute() {
        Memento<MapModel> backup = mapModel.backup();

        Map map = mapRepository.get(mapKey);
        mapModel.setMap(map);

        return CommandResult.successWithRollback(() -> backup.restoreStateOf(mapModel));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {
        private final MapRepository mapRepository;
        private final MapModel mapModel;

        public Command create(MapRepository.MapKey mapKey) {
            return new LoadMapCommand(mapModel, mapRepository, mapKey);
        }
    }
}
