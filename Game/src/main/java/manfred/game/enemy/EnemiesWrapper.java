package manfred.game.enemy;

import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Component
public class EnemiesWrapper implements PaintablesContainer {
    private final List<Enemy> enemies = Collections.synchronizedList(new LinkedList<>());

    public void setEnemies(List<Enemy> enemies) {
        getEnemies().clear();
        getEnemies().addAll(Collections.synchronizedList(enemies));
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void removeKilled() {
        getEnemies().forEach(enemy -> {
            if (enemy.getHealthPoints() <= 0) {
                getEnemies().remove(enemy);
            }
        });
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();
        getEnemies().forEach(enemy -> elements.push(new PaintableContainerElement(enemy, enemy.getBottomLeft())));
        return elements;
    }
}
