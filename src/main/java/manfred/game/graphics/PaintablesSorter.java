package manfred.game.graphics;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

public class PaintablesSorter {
    public TreeMap<Integer, TreeMap<Integer, Paintable>> sortByYAndX(List<PaintablesContainer> paintablesContainers) {
        TreeMap<Integer, TreeMap<Integer, Paintable>> paintablesSortedByYAndX = new TreeMap<>();
        paintablesContainers.forEach(
            paintableContainer -> paintableContainer.getPaintableContainerElements().forEach(
                paintableContainerElement -> insertPaintableIntoSortedMap(paintablesSortedByYAndX, paintableContainerElement)
            )
        );
        return paintablesSortedByYAndX;
    }

    private void insertPaintableIntoSortedMap(TreeMap<Integer, TreeMap<Integer, Paintable>> paintablesSortedByYAndX, PaintableContainerElement paintableContainerElement) {
        TreeMap<Integer, Paintable> paintablesAtY = paintablesSortedByYAndX.computeIfAbsent(
            paintableContainerElement.getY(),
            insertNewTreeMapAtY()
        );
        paintablesAtY.put(
            paintableContainerElement.getX(),
            paintableContainerElement.getPaintable()
        );
    }

    private Function<Integer, TreeMap<Integer, Paintable>> insertNewTreeMapAtY() {
        return y -> new TreeMap<>();
    }
}
