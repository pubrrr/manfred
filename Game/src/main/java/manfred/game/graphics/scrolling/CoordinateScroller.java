package manfred.game.graphics.scrolling;

public interface CoordinateScroller {
    int computeScrollDistance(int coordinate);

    static CoordinateScroller buildFrom(int triggerScrollDistanceToBorder, int windowSize, int mapSize, int coordinateToCenterTo) {
        if (mapSize <= windowSize) {
            return new ConstantScroller((windowSize - mapSize) / 2);
        }

        BoundedDistance.Factory distanceFactory = BoundedDistance.factory(new Interval(windowSize - mapSize, 0));
        BoundedDistance initialScrollDistance = distanceFactory.createOfWithinBounds(windowSize / 2 - coordinateToCenterTo);
        return new DynamicScroller(triggerScrollDistanceToBorder, windowSize, distanceFactory, initialScrollDistance);
    }

    default CoordinateScroller inverted() {
        return new InvertedScroller(this);
    }
}
