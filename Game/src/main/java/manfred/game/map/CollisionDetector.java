package manfred.game.map;

import manfred.game.geometry.Rectangle;

@FunctionalInterface
public interface CollisionDetector {

    boolean isAreaAccessible(Rectangle<Map.Coordinate> area);
}
