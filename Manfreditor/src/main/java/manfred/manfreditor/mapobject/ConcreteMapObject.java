package manfred.manfreditor.mapobject;

public class ConcreteMapObject implements MapObject {

    private final String name;

    public ConcreteMapObject(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
