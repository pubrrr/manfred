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
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

public class Manfred extends MovingObject implements Paintable {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final int INTERACT_DISTANCE = 10;
    private static final int NEXT_ANIMATION_IMAGE_TRIGGER = 5;

    private int healthPoints;
    private MapWrapper mapWrapper;
    private AttacksContainer attacksContainer;
    private SkillSet skillSet;
    private GameConfig gameConfig;
    private HashMap<Direction, BufferedImage[]> walkAnimation;

    private boolean castMode = false;
    private int framesCounter = 0;
    private int animationPosition = 0;

    public Manfred(
            int speed,
            int x,
            int y,
            int healthPoints,
            MapCollider collider,
            MapWrapper mapWrapper,
            AttacksContainer attacksContainer,
            SkillSet skillSet,
            GameConfig gameConfig,
            HashMap<Direction, BufferedImage[]> walkAnimation
    ) {
        super(speed, x, y, gameConfig.getPixelBlockSize(), 2 * gameConfig.getPixelBlockSize(), gameConfig.getPixelBlockSize(), null, collider);
        this.healthPoints = healthPoints;
        this.mapWrapper = mapWrapper;
        this.attacksContainer = attacksContainer;
        this.skillSet = skillSet;
        this.gameConfig = gameConfig;
        this.walkAnimation = walkAnimation;
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
        g.drawImage(walkAnimation.get(viewDirection)[animationPosition], sprite.x, sprite.y, sprite.width, sprite.height, null);

        framesCounter++;
        if (framesCounter >= NEXT_ANIMATION_IMAGE_TRIGGER) {
            framesCounter = 0;
            animationPosition++;
            if (animationPosition >= ANIMATION_IMAGES_NUMBER) {
                animationPosition = 0;
            }
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
                triggerInteractPositionY = this.sprite.getBaseTop() - INTERACT_DISTANCE;
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

    public void castModeOn() {
        this.castMode = true;
    }
}
