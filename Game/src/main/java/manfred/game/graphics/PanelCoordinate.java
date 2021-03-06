package manfred.game.graphics;

import lombok.EqualsAndHashCode;
import lombok.Value;
import manfred.game.geometry.Coordinate;
import manfred.game.geometry.Vector;

@Value
@EqualsAndHashCode
public class PanelCoordinate implements Comparable<PanelCoordinate>, Coordinate<PanelCoordinate> {
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

    public PanelCoordinate translate(Vector<PanelCoordinate> translation) {
        return new PanelCoordinate(this.x + translation.x(), this.y - translation.y());
    }

    @Override
    public Vector<PanelCoordinate> distanceTo(PanelCoordinate other) {
        return Vector.of(other.x - this.x, -other.y + this.y);
    }
}
