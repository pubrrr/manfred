package manfred.manfreditor.controller.command;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import manfred.manfreditor.newmapobject.model.export.MapObjectExporter;
import org.springframework.stereotype.Component;

import java.io.File;

import static manfred.manfreditor.controller.command.CommandResult.failure;
import static manfred.manfreditor.controller.command.CommandResult.success;

@AllArgsConstructor
public class CreateMapObjectCommand implements Command {

    private final MapObjectExporter mapObjectExporter;
    private final LoadMapObjectCommand.Factory loadMapObjectCommandFactory;
    private final NewMapObjectData newMapObjectData;

    @Override
    public CommandResult execute() {
        return mapObjectExporter.export(newMapObjectData)
            .flatMap(this::loadCreatedFiles)
            .fold(
                exception -> failure(exception.getMessage()),
                savedFilesAndLoadObjectRollback -> success(
                    deleteFiles(savedFilesAndLoadObjectRollback._1).andThen(savedFilesAndLoadObjectRollback._2)
                )
            );
    }

    private Try<Tuple2<List<File>, RollbackOperation>> loadCreatedFiles(List<File> createdFiles) {
        return loadMapObjectCommandFactory
            .create(
                createdFiles.filter(file -> file.getName().endsWith("yaml")).head(),
                createdFiles.filter(file -> file.getName().endsWith("png")).head()
            )
            .execute()
            .toTry()
            .onFailure(e -> createdFiles.forEach(File::delete))
            .map(rollbackOperation -> Tuple.of(createdFiles, rollbackOperation));
    }

    private RollbackOperation deleteFiles(List<File> createdFiles) {
        return () -> createdFiles.forEach(File::delete);
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapObjectExporter mapObjectExporter;
        private final LoadMapObjectCommand.Factory loadMapObjectCommandFactory;

        public Command create(NewMapObjectData newMapObjectData) {
            return new CreateMapObjectCommand(mapObjectExporter, loadMapObjectCommandFactory, newMapObjectData);
        }
    }
}
