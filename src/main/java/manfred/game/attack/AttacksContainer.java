package manfred.game.attack;

import manfred.game.graphics.Paintable;

import java.awt.*;
import java.util.LinkedList;

public class AttacksContainer extends LinkedList<Attack> implements Paintable {
    @Override
    public void paint(Graphics g) {
        this.forEach(attack -> attack.paint(g));
    }
}
