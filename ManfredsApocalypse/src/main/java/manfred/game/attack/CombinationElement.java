package manfred.game.attack;

import java.awt.event.KeyEvent;
import java.util.Optional;

public enum CombinationElement {
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),;

    private final int key;

    CombinationElement(int key) {
        this.key = key;
    }

    public static Optional<CombinationElement> fromKeyEvent(KeyEvent event) {
        for (CombinationElement element: CombinationElement.values()) {
            if (element.key == event.getKeyCode()) {
                return Optional.of(element);
            }
        }

        return Optional.empty();
    }
}
