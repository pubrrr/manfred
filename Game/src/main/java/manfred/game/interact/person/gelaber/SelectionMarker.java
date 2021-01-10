package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;

import java.awt.*;

public class SelectionMarker {

    private final Polygon marker;

    public SelectionMarker(GameConfig gameConfig, int position) {
        // TODO: hübsche Geometrie: Berechne Koordinaten anhand von offsetX, offsetY, heigt, width (Basis bis zur rechten Ecke)
        int[] selectionMarkerCornersX = new int[]{
            gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() / 2,
            gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() / 2,
            gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox() * 4 / 5
        };
        int[] selectionMarkerCornersY = new int[]{
            gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6 - gameConfig.getTextPointSize() / 2,
            gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6 + gameConfig.getTextPointSize() / 2,
            gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + gameConfig.getTextPointSize() / 6
        };
        marker = new Polygon(selectionMarkerCornersX, selectionMarkerCornersY, 3);

        marker.translate(0, position * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()));
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(marker);
    }
}
