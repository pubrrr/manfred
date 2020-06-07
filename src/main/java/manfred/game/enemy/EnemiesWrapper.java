package manfred.game.enemy;

import manfred.game.graphics.Paintable;

import java.awt.*;

public class EnemiesWrapper implements Paintable {
    private EnemyStack enemies;

    public void setEnemies(EnemyStack enemies) {
        this.enemies = enemies;
    }

    public EnemyStack getEnemies() {
        return enemies;
    }

    @Override
    public void paint(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.paint(g);
        }
    }
}
