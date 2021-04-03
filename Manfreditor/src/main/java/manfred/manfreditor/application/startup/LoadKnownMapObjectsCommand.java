package manfred.manfreditor.application.startup;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.RawMapTileDtoReader;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapObjectCommand;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class LoadKnownMapObjectsCommand implements Command {

    private final LoadMapObjectCommand.Factory loadMapObjectCommandFactory;
    private final FileHelper fileHelper;

    @Override
    public CommandResult execute() {
        var mapObjectsDirectory = new File(RawMapTileDtoReader.class.getResource("/maps/tiles").getFile());
        File[] files = fileHelper.getFilesIn(mapObjectsDirectory);
        if (files == null) {
            return CommandResult.failure("no files found in directory " + mapObjectsDirectory);
        }
        List.of(files)
            .groupBy(file -> stripFileExtension(file.getName()))
            .values()
            .filter(filesWithSameName -> filesWithSameName.find(file -> file.getName().endsWith("yaml")).isDefined())
            .filter(filesWithSameName -> filesWithSameName.find(file -> file.getName().endsWith("png")).isDefined())
            .map(filesWithSameName -> loadMapObjectCommandFactory.create(
                filesWithSameName.filter(file -> file.getName().endsWith("yaml")).head(),
                filesWithSameName.filter(file -> file.getName().endsWith("png")).head()
            ))
            .forEach(Command::execute);
        return CommandResult.successWithRollback(() -> {});
    }

    private String stripFileExtension(String fileName) {
        List<String> splitOnDots = List.of(fileName.split("\\."));
        List<String> namePartsWithoutExtension = splitOnDots.removeAt(splitOnDots.size() - 1);
        return String.join(".", namePartsWithoutExtension);
    }
}
