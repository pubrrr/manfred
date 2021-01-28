package manfred.game.graphics;

import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

@Component
public class PaintablesSorter {
    public TreeMap<Integer, TreeMap<Integer, LocatedPaintable>> sortByYAndX(List<PaintablesContainer> paintablesContainers) {
        TreeMap<Integer, TreeMap<Integer, LocatedPaintable>> paintablesSortedByYAndX = new TreeMap<>();
        paintablesContainers.forEach(
            paintableContainer -> paintableContainer.getPaintableContainerElements().forEach(
                paintableContainerElement -> insertPaintableIntoSortedMap(paintablesSortedByYAndX, paintableContainerElement)
            )
        );
        return paintablesSortedByYAndX;
    }

    private void insertPaintableIntoSortedMap(TreeMap<Integer, TreeMap<Integer, LocatedPaintable>> paintablesSortedByYAndX, PaintableContainerElement paintableContainerElement) {
        TreeMap<Integer, LocatedPaintable> paintablesAtY = paintablesSortedByYAndX.computeIfAbsent(
            paintableContainerElement.getY(),
            insertNewTreeMapAtY()
        );
        paintablesAtY.put(
            paintableContainerElement.getX(),
            paintableContainerElement.getLocatedPaintable()
        );
    }

    private Function<Integer, TreeMap<Integer, LocatedPaintable>> insertNewTreeMapAtY() {
        return y -> new TreeMap<>();
    }
}
