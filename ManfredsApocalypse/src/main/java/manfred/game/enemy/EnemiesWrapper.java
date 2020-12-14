package manfred.game.enemy;

import manfred.game.graphics.PaintablesContainer;
import manfred.game.graphics.PaintableContainerElement;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Stack;

@Component
public class EnemiesWrapper implements PaintablesContainer, Iterable<Enemy> {
    private EnemyStack enemies;

    public void setEnemies(EnemyStack enemies) {
        this.enemies = enemies;
    }

    @Override
    @NonNull
    public Iterator<Enemy> iterator() {
        return enemies.iterator();
    }

    public void removeKilled() {
        forEach(enemy -> {
            if (enemy.getHealthPoints() <= 0) {
                enemies.remove(enemy);
            }
        });
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();
        forEach(enemy -> elements.push(new PaintableContainerElement(enemy, enemy.getX(), enemy.getY())));
        return elements;
    }
}
