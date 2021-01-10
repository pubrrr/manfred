package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Selector implements Paintable {

    private Choice currentSelection;

    private Selector(Choice initialSelection) {
        this.currentSelection = initialSelection;
    }

    public static Selector fromEdges(List<GelaberEdge> edges, GameConfig gameConfig) {
        AtomicInteger position = new AtomicInteger();
        List<Choice> choices = edges.stream()
            .map(edge -> new Choice(edge, new SelectionMarker(gameConfig, position.getAndIncrement())))
            .collect(Collectors.toList());

        Iterator<Choice> iterator = choices.iterator();
        Choice first = iterator.next();
        Choice previous = first;
        Choice current = first;
        while (iterator.hasNext()) {
            current = iterator.next();
            current.linkWithPrevious(previous);
            previous = current;
        }
        current.next = first;
        first.previous = current;

        return new Selector(first);
    }

    public void selectPrevious() {
        this.currentSelection = this.currentSelection.previous;
    }

    public void selectNext() {
        this.currentSelection = this.currentSelection.next;
    }

    public GelaberEdge confirm() {
        return this.currentSelection.edge;
    }

    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        this.currentSelection.paint(g, offset, x, y);
    }

    private static class Choice implements Paintable {
        private Choice next;
        private Choice previous;
        private final GelaberEdge edge;
        private final SelectionMarker selectionMarker;

        private Choice(GelaberEdge edge, SelectionMarker selectionMarker) {
            this.edge = edge;
            this.selectionMarker = selectionMarker;
        }

        private void linkWithPrevious(Choice previous) {
            this.previous = previous;
            previous.next = this;
        }

        @Override
        public void paint(Graphics g, Point offset, Integer x, Integer y) {
            selectionMarker.paint(g);
        }
    }
}
