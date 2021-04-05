package manfred.manfreditor.application.startup;

import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.MapSource;
import manfred.data.persistence.reader.RawMapTileDtoReader;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.MapProvider;
import manfred.manfreditor.map.model.MapRepository;
import org.springframework.stereotype.Component;

import java.io.File;

import static io.vavr.API.Try;

@Component
@AllArgsConstructor
public class LoadKnownMapsCommand implements Command {

    private final FileHelper fileHelper;
    private final MapProvider mapProvider;
    private final MapRepository mapRepository;

    @Override
    public CommandResult execute() {
        var mapsDirectory = new File(RawMapTileDtoReader.class.getResource("/maps").getFile());
        File[] files = fileHelper.getFilesIn(mapsDirectory);
        if (files == null) {
            return CommandResult.failure("no files found in directory " + mapsDirectory);
        }

        List.of(files)
            .map(MapSource::new)
            .map(mapSource -> Try(() -> mapProvider.provide(mapSource)))
            .filter(Try::isSuccess)
            .map(Try::get)
            .forEach(mapRepository::populateWith);
        return CommandResult.successWithRollback(() -> {});
    }
}
