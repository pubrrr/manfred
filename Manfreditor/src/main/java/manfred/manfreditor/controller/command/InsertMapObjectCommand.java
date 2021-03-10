package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

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
            .map(this::getClickedTileAndInsertObject)
            .orElse(CommandResult.failure("Need to select an object before inserting it into the map"));
    }

    private CommandResult getClickedTileAndInsertObject(ConcreteMapObject concreteMapObject) {
        return mapView.getClickedTile(this.x, this.y)
            .map(insertObject(concreteMapObject))
            .orElse(CommandResult.failure("No map tile at clicked coordinates (" + this.x + "," + this.y + ") was found"));
    }

    private Function<Map.TileCoordinate, CommandResult> insertObject(ConcreteMapObject concreteMapObject) {
        return tileCoordinate -> {
            List<String> validationMessages = mapModel.tryInsertObjectAt(concreteMapObject, tileCoordinate);
            return validationMessages.isEmpty()
                ? CommandResult.success(() -> mapModel.deleteObjectAt(tileCoordinate))
                : CommandResult.failure(String.join(",\n", validationMessages));
        };
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
