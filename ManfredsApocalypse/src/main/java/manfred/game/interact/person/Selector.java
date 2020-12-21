package manfred.game.interact.person;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Selector {

    private Choice currentSelection;

    private Selector(Choice initialSelection) {
        this.currentSelection = initialSelection;
    }

    public static Selector fromEdges(List<GelaberEdge> edges) {
        List<Choice> choices = edges.stream()
            .map(GelaberEdge::follow)
            .map(Choice::new)
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

    public GelaberNodeIdentifier confirm() {
        return this.currentSelection.node;
    }

    public boolean isSelected(GelaberNodeIdentifier nodeIdentifier) {
        return currentSelection.node.equals(nodeIdentifier);
    }

    private static class Choice {
        private Choice next;
        private Choice previous;
        private final GelaberNodeIdentifier node;

        private Choice(GelaberNodeIdentifier node) {
            this.node = node;
        }

        private void linkWithPrevious(Choice previous) {
            this.previous = previous;
            previous.next = this;
        }
    }
}
