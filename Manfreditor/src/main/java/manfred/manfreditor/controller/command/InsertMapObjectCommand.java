package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class InsertMapObjectCommand implements Command {

    private final MapView mapView;
    private final MapModel mapModel;
    private final SelectedObject selectedObject;
    private final MapObjectRepository mapObjectRepository;
    private final int x;
    private final int y;

    @Override
    public CommandResult execute() {
        return selectedObject.getSelection()
            .map(mapObjectRepository::get)
            .map(this::insertCorrespondingObject)
            .orElse(CommandResult.failure("Need to select an object before inserting it into the map"));
    }

    private CommandResult insertCorrespondingObject(ConcreteMapObject concreteMapObject) {
        return mapView.getClickedTile(this.x, this.y)
            .map(tileCoordinate -> mapModel.insertObjectAt(concreteMapObject, tileCoordinate))
            .map(validationMessages -> validationMessages.isEmpty()
                ? CommandResult.success()
                : CommandResult.failure(String.join(", ", validationMessages))
            )
            .orElse(CommandResult.failure("No map tile at clicked coordinates (" + this.x + "," + this.y + ") was found"));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapView mapView;
        private final MapModel mapModel;
        private final SelectedObject selectedObject;
        private final MapObjectRepository mapObjectRepository;

        public Command create(int x, int y) {
            return new InsertMapObjectCommand(mapView, mapModel, selectedObject, mapObjectRepository, x, y);
        }
    }
}
