package manfred.manfreditor.controller.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadMapCommand implements Command {

    private final MapModel mapModel;
    private final MapProvider mapProvider;
    private final String mapPath;
    private final CommandUrlHelper urlHelper;

    @Override
    public CommandResult execute() {
        Memento<MapModel> backup = mapModel.backup();

        URL mapUrl;
        try {
            mapUrl = urlHelper.getUrlForFile(this.mapPath);
        } catch (MalformedURLException e) {
            return CommandResult.failure("Could not create URL for map file " + mapPath + ": " + e.getMessage());
        }

        try {
            Map newMap = mapProvider.provide(new MapSource(mapUrl));
            mapModel.setMap(newMap);
        } catch (InvalidInputException e) {
            return CommandResult.failure(e.getMessage());
        }
        return CommandResult.success(() -> backup.restoreStateOf(mapModel));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {
        private final MapProvider mapProvider;
        private final MapModel mapModel;
        private final CommandUrlHelper urlHelper;

        public Command create(String mapName) {
            return new LoadMapCommand(mapModel, mapProvider, mapName, urlHelper);
        }
    }
}
