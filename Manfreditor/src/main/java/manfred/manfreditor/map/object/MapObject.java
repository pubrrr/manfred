package manfred.manfreditor.map.object;

public interface MapObject {
    static MapObject none() {
        return new None();
    }
}
