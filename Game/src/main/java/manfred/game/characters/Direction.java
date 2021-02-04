package manfred.game.characters;

import manfred.game.attack.Attack;
import manfred.game.map.Vector;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum Direction {
    RIGHT(interactRight(), Attack::right, Vector.pointingRight(100)),
    LEFT(interactLeft(), Attack::left, Vector.pointingRight(-100)),
    UP(interactUp(), Attack::up, Vector.pointingUp(100)),
    DOWN(interactDown(), Attack::down, Vector.pointingUp(-100));

    private final BiFunction<Sprite, Integer, Point> interactionPointDeterminator;
    private final Consumer<Attack> initialAttackMovement;
    private final Vector vector;

    Direction(BiFunction<Sprite, Integer, Point> interactionPointDeterminator, Consumer<Attack> initialAttackMovement, Vector vector) {
        this.interactionPointDeterminator = interactionPointDeterminator;
        this.initialAttackMovement = initialAttackMovement;
        this.vector = vector;
    }

    public Vector getVector() {
        return this.vector;
    }

    public void kickAttack(Attack attack) {
        this.initialAttackMovement.accept(attack);
    }

    public Point interactAtDistance(Sprite sprite, int interactionDistance) {
        return this.interactionPointDeterminator.apply(sprite, interactionDistance);
    }

    private static BiFunction<Sprite, Integer, Point> interactRight() {
        return (Sprite sprite, Integer interactionDistance) -> new Point(
            sprite.getRight() + interactionDistance,
            sprite.getCenter().y
        );
    }

    private static BiFunction<Sprite, Integer, Point> interactLeft() {
        return (Sprite sprite, Integer interactionDistance) -> new Point(
            sprite.getLeft() - interactionDistance,
            sprite.getCenter().y
        );
    }

    private static BiFunction<Sprite, Integer, Point> interactUp() {
        return (Sprite sprite, Integer interactionDistance) -> new Point(
            sprite.getCenter().x,
            sprite.getBaseTop() - interactionDistance
        );
    }

    private static BiFunction<Sprite, Integer, Point> interactDown() {
        return (Sprite sprite, Integer interactionDistance) -> new Point(
            sprite.getCenter().x,
            sprite.getBottom() + interactionDistance
        );
    }
}
