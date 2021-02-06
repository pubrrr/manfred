package manfred.game.graphics;

import lombok.Value;

@Value
public class PanelCoordinate implements Comparable<PanelCoordinate> {
    int x;
    int y; // (0,0) = top left corner, y-axis is oriented downwards

    @Override
    public int compareTo(PanelCoordinate other) {
        int result = Integer.compare(this.y, other.y);
        if (result == 0) {
            result = Integer.compare(this.x, other.x);
        }
        return result;
    }
}
