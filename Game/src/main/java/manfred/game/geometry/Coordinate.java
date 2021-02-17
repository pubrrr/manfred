package manfred.game.geometry;

public interface Coordinate<C extends Coordinate<C>> {

    C translate(Vector<C> translation);

    Vector<C> distanceTo(C coordinate);
}
