package manfred.manfreditor.map.controller.command;

import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.view.map.MapView;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import manfred.manfreditor.map.model.mapobject.MapObjectRepository;
import manfred.manfreditor.map.model.mapobject.SelectedObject;
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
            Validation<List<String>, ConcreteMapObject> validation = mapModel.tryInsertObjectAt(concreteMapObject, tileCoordinate);
            return validation.fold(
                errorMessages -> CommandResult.failure(String.join(",\n", errorMessages)),
                ignoreThis -> CommandResult.successWithRollback(() -> mapModel.deleteObjectAt(tileCoordinate))
            );
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
