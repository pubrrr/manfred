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
    public void paint(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.paint(g);
        }
    }

    @Override
    public Iterator<Enemy> iterator() {
        return enemies.iterator();
    }
}
