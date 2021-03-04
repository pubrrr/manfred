package manfred.manfreditor.gui.view.map;

import lombok.Value;

@Value
public class MapViewCoordinate implements Comparable<MapViewCoordinate> {
    int x;
    int y;

    @Override
    public int compareTo(MapViewCoordinate other) {
        int result = Integer.compare(this.y, other.y);
        if (result == 0) {
            result = Integer.compare(this.x, other.x);
        }
        return result;
    }
}
