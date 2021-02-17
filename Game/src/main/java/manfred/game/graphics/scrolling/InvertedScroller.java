package manfred.game.graphics.scrolling;

public class InvertedScroller implements CoordinateScroller {

    private final CoordinateScroller scrollerToInvert;

    public InvertedScroller(CoordinateScroller scrollerToInvert) {
        this.scrollerToInvert = scrollerToInvert;
    }

    @Override
    public int computeScrollDistance(int coordinate) {
        return -scrollerToInvert.computeScrollDistance(coordinate);
    }
}
