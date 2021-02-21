package manfred.manfreditor.mapobject;

public interface MapObject {
    static MapObject none() {
        return new None();
    }
}
