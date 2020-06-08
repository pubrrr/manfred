package manfred.game.attack;

import manfred.game.enemy.Enemy;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.util.LinkedList;

public class AttacksContainer extends LinkedList<Attack> implements Paintable {
    @Override
    public void paint(Graphics g) {
        this.forEach(attack -> attack.paint(g));
    }

    public void hit(Enemy enemy) {
        this.forEach(attack -> attack.hit(enemy));
    }

    public void removeResolved() {
        forEach(attack -> {
            if (attack.isResolved()) {
                remove(attack);
            }
        });
    }
}
