package manfred.game.graphics;

import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.graphics.paintable.PaintablesContainer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class PaintablesSorter {

    public SortedMap<PanelCoordinate, List<LocatedPaintable>> sortByYAndX(List<PaintablesContainer> paintablesContainers, MapCoordinateToPanelCoordinateTransformer coordinateTransformer) {
        return paintablesContainers.stream()
            .map(PaintablesContainer::getPaintableContainerElements)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(
                paintableContainerElement -> coordinateTransformer.toPanelCoordinate(paintableContainerElement.getCoordinate()),
                paintableContainerElement -> List.of(paintableContainerElement.getLocatedPaintable()),
                this::mergeLists,
                TreeMap::new
            ));
    }

    private <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        LinkedList<T> combinedList = new LinkedList<>(list1);
        combinedList.addAll(list2);
        return Collections.unmodifiableList(combinedList);
    }
}
