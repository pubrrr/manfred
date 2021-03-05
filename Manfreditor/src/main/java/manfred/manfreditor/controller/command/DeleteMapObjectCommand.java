package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.map.MapModel;
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
            .map(tileCoordinate -> mapModel.deleteObjectAt(tileCoordinate)
                ? CommandResult.success()
                : CommandResult.failure("No object found to delete at tile coordinate (" + tileCoordinate.getX() + "," +  tileCoordinate.getY() + ")")
            )
            .orElse(CommandResult.failure("No map tile at clicked coordinates (" + this.x + "," + this.y + ") was found"));
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
