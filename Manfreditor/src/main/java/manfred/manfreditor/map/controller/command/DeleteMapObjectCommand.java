package manfred.manfreditor.map.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.view.map.MapView;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class DeleteMapObjectCommand implements Command {

    private final MapView mapView;
    private final MapModel mapModel;
    private final int x;
    private final int y;

    @Override
    public CommandResult execute() {
        return mapView.getClickedTile(this.x, this.y)
            .map(this::deleteObjectAt)
            .orElse(CommandResult.failure("No map tile at clicked coordinates (" + this.x + "," + this.y + ") was found"));
    }

    private CommandResult deleteObjectAt(Map.TileCoordinate tileCoordinate) {
        return mapModel.deleteObjectAt(tileCoordinate)
            .map(locatedMapObject -> CommandResult.successWithRollback(() -> mapModel.forceInsertObjectAt(locatedMapObject)))
            .orElse(CommandResult.failure("No object could be deleted at tile (" + tileCoordinate.getX() + "," + tileCoordinate.getY() + ")"));
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapView mapView;
        private final MapModel mapModel;

        public Command create(int x, int y) {
            return new DeleteMapObjectCommand(mapView, mapModel, x, y);
        }
    }
}
