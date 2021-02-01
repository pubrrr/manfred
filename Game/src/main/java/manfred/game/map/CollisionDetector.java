package manfred.game.map;

@FunctionalInterface
public interface CollisionDetector {

    boolean isAreaAccessible(Rectangle area);
}
