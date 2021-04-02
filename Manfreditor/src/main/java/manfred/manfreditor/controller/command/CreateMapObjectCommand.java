package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.mapobject.NewMapObjectData;
import manfred.manfreditor.mapobject.export.MapObjectExporter;
import org.springframework.stereotype.Component;

import java.io.File;

import static manfred.manfreditor.controller.command.CommandResult.*;
import static manfred.manfreditor.controller.command.CommandResult.failure;

@AllArgsConstructor
public class CreateMapObjectCommand implements Command {

    private final MapObjectExporter mapObjectExporter;
    private final NewMapObjectData newMapObjectData;

    @Override
    public CommandResult execute() {
        return mapObjectExporter.export(newMapObjectData)
            .fold(
                exception -> failure(exception.getMessage()),
                savedFiles -> success(() -> savedFiles.forEach(File::delete))
            );
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
