package manfred.game.characters;

import manfred.game.attack.Attack;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum Direction {
    RIGHT(interactRight(), Attack::right),
    LEFT(interactLeft(), Attack::left),
    UP(interactUp(), Attack::up),
    DOWN(interactDown(), Attack::down);

    private final BiFunction<Sprite, Integer, Point> interactionPointDeterminator;
    private final Consumer<Attack> initialAttackMovement;

    Direction(BiFunction<Sprite, Integer, Point> interactionPointDeterminator, Consumer<Attack> initialAttackMovement) {
        this.interactionPointDeterminator = interactionPointDeterminator;
        this.initialAttackMovement = initialAttackMovement;
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
