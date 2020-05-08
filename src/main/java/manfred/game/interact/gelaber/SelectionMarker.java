package manfred.game.interact.gelaber;

import java.awt.*;
import java.util.Arrays;

public class SelectionMarker extends Polygon {
    // TODO: hübsche Geometrie: Berechne Koordinaten anhand von offsetX, offsetY, heigt, width (Basis bis zur rechten Ecke)
    public final static int[] SELECTION_MARKER_CORNERS_X = new int[]{
            Gelaber.GELABER_BOX_POSITION_X + Gelaber.TEXT_DISTANCE_TO_BOX / 2,
            Gelaber.GELABER_BOX_POSITION_X + Gelaber.TEXT_DISTANCE_TO_BOX / 2,
            Gelaber.GELABER_BOX_POSITION_X + Gelaber.TEXT_DISTANCE_TO_BOX * 4 / 5
    };
    public final static int[] SELECTION_MARKER_CORNERS_Y = new int[]{
            Gelaber.GELABER_BOX_POSITION_Y + Gelaber.TEXT_DISTANCE_TO_BOX + Gelaber.TEXT_POINT_SIZE / 6 - Gelaber.TEXT_POINT_SIZE / 2,
            Gelaber.GELABER_BOX_POSITION_Y + Gelaber.TEXT_DISTANCE_TO_BOX + Gelaber.TEXT_POINT_SIZE / 6 + Gelaber.TEXT_POINT_SIZE / 2,
            Gelaber.GELABER_BOX_POSITION_Y + Gelaber.TEXT_DISTANCE_TO_BOX + Gelaber.TEXT_POINT_SIZE / 6
    };

    public SelectionMarker() {
        super(SELECTION_MARKER_CORNERS_X, SELECTION_MARKER_CORNERS_Y, 3);
    }

    public void resetToTop() {
        reset();
        this.npoints = 3;
        this.xpoints = Arrays.copyOf(SELECTION_MARKER_CORNERS_X, this.npoints);
        this.ypoints = Arrays.copyOf(SELECTION_MARKER_CORNERS_Y, this.npoints);
    }
}
