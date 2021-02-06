package manfred.game.graphics;

import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class PaintablesSorter {
    public SortedMap<PanelCoordinate, LocatedPaintable> sortByYAndX(List<PaintablesContainer> paintablesContainers, MapCoordinateToPanelCoordinateTransformer coordinateTransformer) {
        return paintablesContainers.stream()
            .map(PaintablesContainer::getPaintableContainerElements)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(
                paintableContainerElement -> coordinateTransformer.toPanelCoordinate(paintableContainerElement.getCoordinate()),
                PaintableContainerElement::getLocatedPaintable,
                (paintable, equalPaintable) -> paintable,
                TreeMap::new
            ));
    }
}
