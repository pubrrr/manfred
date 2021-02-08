package manfred.game.graphics.scrolling;

public class DynamicScroller implements CoordinateScroller {

    private final int triggerScrollDistanceToBorder;
    private final int windowSize;
    private final BoundedDistance.Factory distanceFactory;

    private BoundedDistance memorizedScrollDistance;

    public DynamicScroller(int triggerScrollDistanceToBorder, int windowSize, BoundedDistance.Factory distanceFactory, BoundedDistance initialScrollDistance) {
        this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
        this.windowSize = windowSize;
        this.distanceFactory = distanceFactory;
        this.memorizedScrollDistance = initialScrollDistance;
    }

    @Override
    public int computeScrollDistance(int coordinate) {
        int distanceToLeftBorder = coordinate + memorizedScrollDistance.value();
        int distanceToRightBorder = windowSize - (coordinate + memorizedScrollDistance.value());

        if (distanceToLeftBorder < triggerScrollDistanceToBorder) {
            int distanceToScroll = memorizedScrollDistance.value() + (triggerScrollDistanceToBorder - distanceToLeftBorder);
            this.memorizedScrollDistance = this.distanceFactory.createOfWithinBounds(distanceToScroll);
        } else if (distanceToRightBorder < triggerScrollDistanceToBorder) {
            int distanceToScroll = memorizedScrollDistance.value() - (triggerScrollDistanceToBorder - distanceToRightBorder);
            this.memorizedScrollDistance = this.distanceFactory.createOfWithinBounds(distanceToScroll);
        }

        return memorizedScrollDistance.value();
    }

}
