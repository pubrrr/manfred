package manfred.game.enemy;

import manfred.game.graphics.Paintable;

import java.awt.*;
import java.util.Iterator;

public class EnemiesWrapper implements Paintable, Iterable<Enemy> {
    private EnemyStack enemies;

    public void setEnemies(EnemyStack enemies) {
        this.enemies = enemies;
    }

    @Override
    public void paint(Graphics g, Point offset) {
        enemies.forEach(enemy -> enemy.paint(g, offset));
    }

    @Override
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
}
