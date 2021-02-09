package manfred.game.graphics;

import manfred.data.shared.PositiveInt;
import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static helpers.TestMapFactory.coordinateAt;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaintablesSorterTest {
    private PaintablesSorter underTest;

    @BeforeEach
    void init() {
        underTest = new PaintablesSorter();
    }

    @Test
    void foreachOverResultWorksObjectInCorrectOrder() {
        PaintableContainerElement first = getPaintableContainerElementMock(1, 20);
        PaintableContainerElement second = getPaintableContainerElementMock(20, 20);
        PaintableContainerElement third = getPaintableContainerElementMock(1, 1);
        PaintableContainerElement fourth = getPaintableContainerElementMock(1, 1);
        PaintableContainerElement fifth = getPaintableContainerElementMock(20, 1);

        LocatedPaintable[] paintablesInExpectedOrder = new LocatedPaintable[]{
            first.getLocatedPaintable(),
            second.getLocatedPaintable(),
            third.getLocatedPaintable(),
            fourth.getLocatedPaintable(),
            fifth.getLocatedPaintable()
        };

        List<PaintablesContainer> input = setupTwoContainersWithElements(first, second, third, fourth, fifth);

        SortedMap<PanelCoordinate, List<LocatedPaintable>> result = underTest.sortByYAndX(input, new MapCoordinateToPanelCoordinateTransformer(PositiveInt.ofNonZero(60)));

        LocatedPaintable[] paintablesInActualOrder = new LocatedPaintable[5];
        AtomicInteger i = new AtomicInteger();
        result.forEach(
            (panelCoordinate, paintables) -> paintables.forEach(
                paintable -> paintablesInActualOrder[i.getAndIncrement()] = paintable
            )
        );

        assertArrayEquals(paintablesInExpectedOrder, paintablesInActualOrder);
    }

    private List<PaintablesContainer> setupTwoContainersWithElements(PaintableContainerElement first, PaintableContainerElement second, PaintableContainerElement third, PaintableContainerElement fourth, PaintableContainerElement fifth) {
        Stack<PaintableContainerElement> stack1 = new Stack<>();
        stack1.push(third);
        stack1.push(first);
        stack1.push(fourth);

        PaintablesContainer container1 = mock(PaintablesContainer.class);
        when(container1.getPaintableContainerElements()).thenReturn(stack1);

        Stack<PaintableContainerElement> stack2 = new Stack<>();
        stack2.push(fifth);
        stack2.push(second);

        PaintablesContainer container2 = mock(PaintablesContainer.class);
        when(container2.getPaintableContainerElements()).thenReturn(stack2);

        List<PaintablesContainer> paintables = new LinkedList<>();
        paintables.add(container1);
        paintables.add(container2);
        return paintables;
    }

    private PaintableContainerElement getPaintableContainerElementMock(int x, int y) {
        return new PaintableContainerElement(mock(LocatedPaintable.class), coordinateAt(x, y));
    }
}