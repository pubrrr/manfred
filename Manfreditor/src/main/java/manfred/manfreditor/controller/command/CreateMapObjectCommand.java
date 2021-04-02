package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.mapobject.NewMapObjectData;
import manfred.manfreditor.mapobject.export.MapObjectExporter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class CreateMapObjectCommand implements Command {

    private final MapObjectExporter mapObjectExporter;
    private final NewMapObjectData newMapObjectData;

    @Override
    public CommandResult execute() {
        return null;
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapObjectExporter mapObjectExporter;

        public Command create(NewMapObjectData newMapObjectData) {
            return new CreateMapObjectCommand(mapObjectExporter, newMapObjectData);
        }
    }
}
