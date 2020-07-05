package manfred.game.interact.gelaber;

import manfred.game.GameConfig;

import java.awt.*;
import java.util.Arrays;

public class SelectionMarker {
    public final int[] selectionMarkerCornersY;

    private Polygon marker;
    public final int[] selectionMarkerCornersX;

    public SelectionMarker(GameConfig gameConfig) {
        // TODO: hübsche Geometrie: Berechne Koordinaten anhand von offsetX, offsetY, heigt, width (Basis bis zur rechten Ecke)
        selectionMarkerCornersX = new int[]{
                gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() / 2,
                gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() / 2,
                gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() * 4 / 5
        };
        selectionMarkerCornersY = new int[]{
                gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6 - gameConfig.getTextPointSize() / 2,
                gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6 + gameConfig.getTextPointSize() / 2,
                gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6
        };
        marker = new Polygon(selectionMarkerCornersX, selectionMarkerCornersY, 3);
    }

    public void translate(int deltaX, int deltaY) {
        marker.translate(deltaX, deltaY);
    }

    public void resetToTop() {
        marker.reset();
        marker.npoints = 3;
        marker.xpoints = Arrays.copyOf(selectionMarkerCornersX, marker.npoints);
        marker.ypoints = Arrays.copyOf(selectionMarkerCornersY, marker.npoints);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(marker);
    }
}
