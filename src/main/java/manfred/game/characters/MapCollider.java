package manfred.game.characters;

import manfred.game.graphics.GamePanel;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;

public class MapCollider {
    private MapWrapper mapWrapper;

    public MapCollider(MapWrapper mapWrapper) {
        this.mapWrapper = mapWrapper;
    }

    public boolean collides(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        int onMapGripLeft = leftBorder / GamePanel.PIXEL_BLOCK_SIZE;
        int onMapGripRight = rightBorder / GamePanel.PIXEL_BLOCK_SIZE;
        int onMapGripTop = topBorder / GamePanel.PIXEL_BLOCK_SIZE;
        int onMapGripBottom = bottomBorder / GamePanel.PIXEL_BLOCK_SIZE;

        Map map = mapWrapper.getMap();
        return !(
                map.isAccessible(onMapGripLeft, onMapGripTop)
                        && map.isAccessible(onMapGripRight, onMapGripTop)
                        && map.isAccessible(onMapGripLeft, onMapGripBottom)
                        && map.isAccessible(onMapGripRight, onMapGripBottom)
        );
    }
}
