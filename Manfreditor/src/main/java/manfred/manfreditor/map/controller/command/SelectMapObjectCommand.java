package manfred.manfreditor.map.controller.command;

import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.view.mapobject.MapObjectsView;
import manfred.manfreditor.map.model.mapobject.MapObjectRepository;
import manfred.manfreditor.map.model.mapobject.SelectedObject;
import org.springframework.stereotype.Component;

public class SelectMapObjectCommand implements Command {

    private final MapObjectsView mapObjectsView;
    private final SelectedObject selectedObject;
    private final int x;
    private final int y;

    public SelectMapObjectCommand(MapObjectsView mapObjectsView, SelectedObject selectedObject, int x, int y) {
        this.mapObjectsView = mapObjectsView;
        this.selectedObject = selectedObject;
        this.x = x;
        this.y = y;
    }

    @Override
    public CommandResult execute() {
        return mapObjectsView.getClickedObjectKey(this.x, this.y)
            .map(this::selectKey)
            .orElse(CommandResult.failure("No map object found at click position (" + this.x + "," + this.y + ")"));
    }

    private CommandResult selectKey(MapObjectRepository.ObjectKey objectKey) {
        var previousSelection = selectedObject.getSelection();
        selectedObject.select(objectKey);
        return CommandResult.successWithRollback(() -> selectedObject.select(previousSelection.orElse(objectKey)));
    }

    @Component
    public static class Factory {

        private final MapObjectsView mapObjectsView;
        private final SelectedObject selectedObject;

        public Factory(MapObjectsView mapObjectsView, SelectedObject selectedObject) {
            this.mapObjectsView = mapObjectsView;
            this.selectedObject = selectedObject;
        }

        public Command create(int x, int y) {
            return new SelectMapObjectCommand(mapObjectsView, selectedObject, x, y);
        }
    }
}

