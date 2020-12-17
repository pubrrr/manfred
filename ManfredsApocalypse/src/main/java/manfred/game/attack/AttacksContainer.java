package manfred.game.attack;

import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.game.graphics.paintable.PaintableContainerElement;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Stack;

@Component
public class AttacksContainer extends LinkedList<Attack> implements PaintablesContainer {
    public void removeResolved() {
        forEach(attack -> {
            if (attack.isResolved()) {
                remove(attack);
            }
        });
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();
        forEach(attack -> elements.push(new PaintableContainerElement(attack, attack.getX(), attack.getY())));
        return elements;
    }
}
