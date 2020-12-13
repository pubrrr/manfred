package manfred.game.characters;

import java.awt.*;
import java.util.function.BiFunction;

public enum Direction {
    RIGHT(interactRight()),
    LEFT(interactLeft()),
    UP(interactUp()),
    DOWN(interactDown());

    private BiFunction<Sprite, Integer, Point> interactionPointDeterminator;

    Direction(BiFunction<Sprite, Integer, Point> interactionPointDeterminator) {
        this.interactionPointDeterminator = interactionPointDeterminator;
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
