package manfred.manfreditor.controller.newmapobject;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.gui.view.mapobject.NewMapObjectView;
import manfred.manfreditor.mapobject.NewMapObjectModel;
import org.eclipse.swt.graphics.Point;
import org.springframework.stereotype.Component;

import static manfred.manfreditor.controller.command.CommandResult.failure;
import static manfred.manfreditor.controller.command.CommandResult.success;

@AllArgsConstructor
public class ClickObjectPreviewCommand implements Command {

    private final NewMapObjectView newMapObjectView;
    private final NewMapObjectModel newMapObjectModel;
    private final int x;
    private final int y;
    private final Point canvasSize;

    @Override
    public CommandResult execute() {
        return newMapObjectView.getClickedTile(this.x, this.y, this.canvasSize)
            .peek(newMapObjectModel::invertAccessibility)
            .fold(
                () -> failure("no tile clicked"),
                previewTileCoordinate -> success(() -> newMapObjectModel.invertAccessibility(previewTileCoordinate))
            );
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final NewMapObjectView newMapObjectView;
        private final NewMapObjectModel newMapObjectModel;

        public ClickObjectPreviewCommand create(int x, int y, Point canvasSize) {
            return new ClickObjectPreviewCommand(newMapObjectView, newMapObjectModel, x, y, canvasSize);
        }
    }
}
