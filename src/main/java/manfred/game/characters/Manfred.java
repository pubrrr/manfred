package manfred.game.characters;

import manfred.game.GameConfig;
import manfred.game.attack.Attack;
import manfred.game.attack.AttackGenerator;
import manfred.game.attack.AttacksContainer;
import manfred.game.controls.KeyControls;
import manfred.game.graphics.Paintable;
import manfred.game.interact.Interactable;
import manfred.game.map.MapWrapper;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.Stack;
import java.util.function.Consumer;

public class Manfred extends MovingObject implements Paintable {
    private static final int INTERACT_DISTANCE = 10;
    private int healthPoints;
    private MapWrapper mapWrapper;
    private AttacksContainer attacksContainer;
    private SkillSet skillSet;
    private GameConfig gameConfig;
    private boolean castMode;

    public Manfred(int speed, int x, int y, int healthPoints, MapCollider collider, MapWrapper mapWrapper, AttacksContainer attacksContainer, SkillSet skillSet, GameConfig gameConfig) {
        super(speed, x, y, gameConfig.getPixelBlockSize(), gameConfig.getPixelBlockSize(), null, collider);
        this.healthPoints = healthPoints;
        this.mapWrapper = mapWrapper;
        this.attacksContainer = attacksContainer;
        this.skillSet = skillSet;
        this.gameConfig = gameConfig;
    }

    public void setX(int x) {
        this.sprite.x = x;
    }

    public void setY(int y) {
        this.sprite.y = y;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> move() {
        super.move();

        Point mainTile = checkForTileManfredStandMainlyOn();
        return mapWrapper.getMap().stepOn(mainTile.x, mainTile.y);
    }

    private Point checkForTileManfredStandMainlyOn() {
        Point center = this.sprite.getCenter();
        return new Point(center.x / gameConfig.getPixelBlockSize(), center.y / gameConfig.getPixelBlockSize());
    }

    @Override
    public void paint(Graphics g, Point offset) {
        if (this.castMode) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillPolygon(this.sprite.toPaint(offset));

        g.setColor(Color.BLACK);
        Point center = this.sprite.getCenter();
        switch (viewDirection) {
            case up:
                g.fillRect(center.x - offset.x, this.sprite.getTop() - offset.y, 10, 10);
                break;
            case down:
                g.fillRect(center.x - offset.x, this.sprite.getBottom() - offset.y, 10, 10);
                break;
            case left:
                g.fillRect(this.sprite.getLeft() - offset.x, center.y - offset.y, 10, 10);
                break;
            case right:
                g.fillRect(this.sprite.getRight() - offset.x, center.y - offset.y, 10, 10);
                break;
        }
    }

    @Nullable
    public Consumer<KeyControls> interact() {
        int triggerInteractPositionX = 0;
        int triggerInteractPositionY = 0;

        Point center = this.sprite.getCenter();
        switch (viewDirection) {
            case up:
                triggerInteractPositionX = center.x;
                triggerInteractPositionY = this.sprite.getTop() - INTERACT_DISTANCE;
                break;
            case down:
                triggerInteractPositionX = center.x;
                triggerInteractPositionY = this.sprite.getBottom() + INTERACT_DISTANCE;
                break;
            case left:
                triggerInteractPositionX = this.sprite.getLeft() - INTERACT_DISTANCE;
                triggerInteractPositionY = center.y;
                break;
            case right:
                triggerInteractPositionX = this.sprite.getRight() + INTERACT_DISTANCE;
                triggerInteractPositionY = center.y;
                break;
        }
        int onMapGridX = triggerInteractPositionX / gameConfig.getPixelBlockSize();
        int onMapGridY = triggerInteractPositionY / gameConfig.getPixelBlockSize();

        Interactable interactable = mapWrapper.getMap().getInteractable(onMapGridX, onMapGridY);
        if (interactable == null) {
            return null;
        }
        return interactable.interact();
    }

    public void cast(Stack<String> attackCombination) {
        this.castMode = false;

        StringBuilder stringBuilder = new StringBuilder();
        attackCombination.forEach(stringBuilder::append);
        AttackGenerator attackGenerator = skillSet.get(stringBuilder.toString());

        if (attackGenerator != null) {
            Attack attack = attackGenerator.generate(this.sprite.getCenter(), this.viewDirection);
            attacksContainer.add(attack);
        }
    }

    public void castMode() {
        this.castMode = true;
    }
}
