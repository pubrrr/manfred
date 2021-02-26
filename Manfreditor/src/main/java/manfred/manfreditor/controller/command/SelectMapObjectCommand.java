package manfred.manfreditor.controller.command;

import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.springframework.stereotype.Component;

public class SelectMapObjectCommand implements Command {

    private final MapObjectRepository mapObjectRepositoryMock;
    private final SelectedObject selectedObjectMock;
    private final String key;

    public SelectMapObjectCommand(MapObjectRepository mapObjectRepositoryMock, SelectedObject selectedObjectMock, String key) {
        this.mapObjectRepositoryMock = mapObjectRepositoryMock;
        this.selectedObjectMock = selectedObjectMock;
        this.key = key;
    }

    @Override
    public CommandResult execute() {
        return this.mapObjectRepositoryMock.getKey(key)
            .map(this::selectKey)
            .orElse(CommandResult.failure("No map object found in repository with key '" + this.key + "'"));
    }

    private CommandResult selectKey(MapObjectRepository.ObjectKey objectKey) {
        selectedObjectMock.select(objectKey);
        return CommandResult.success();
    }

    @Component
    public static class Factory {

        private final MapObjectRepository mapObjectRepositoryMock;
        private final SelectedObject selectedObjectMock;

        public Factory(MapObjectRepository mapObjectRepositoryMock, SelectedObject selectedObjectMock) {
            this.mapObjectRepositoryMock = mapObjectRepositoryMock;
            this.selectedObjectMock = selectedObjectMock;
        }

        public SelectMapObjectCommand create(String key) {
            return new SelectMapObjectCommand(mapObjectRepositoryMock, selectedObjectMock, key);
        }
    }
}

