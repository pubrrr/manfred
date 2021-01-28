package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Selector {

    private Choice currentSelection;

    private Selector(Choice initialSelection) {
        this.currentSelection = initialSelection;
    }

    public static Selector fromEdges(List<GelaberEdge> edges, GameConfig gameConfig) {
        AtomicInteger position = new AtomicInteger();
        List<Choice> choices = edges.stream()
            .map(edge -> new Choice(edge, new SelectionMarker(gameConfig, position.getAndIncrement())))
            .collect(Collectors.toList());

        Choice first = linkAllChoices(choices);

        return new Selector(first);
    }

    private static Choice linkAllChoices(List<Choice> choices) {
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
        return first;
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

    public void paint(Graphics g) {
        this.currentSelection.paint(g);
    }

    private static class Choice {
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

        public void paint(Graphics g) {
            selectionMarker.paint(g);
        }
    }
}
