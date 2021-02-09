package manfred.game.graphics.scrolling;

public class ConstantScroller implements CoordinateScroller {
    private final int constantScrollDistance;

    public ConstantScroller(int constantScrollDistance) {
        this.constantScrollDistance = constantScrollDistance;
    }

    @Override
    public int computeScrollDistance(int coordinate) {
        return constantScrollDistance;
    }
}
