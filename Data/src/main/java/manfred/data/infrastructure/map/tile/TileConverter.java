package manfred.data.infrastructure.map.tile;

public abstract class TileConverter {

    abstract public TilePrototype stringToObject(String tileValue);

    protected boolean isNotAccessible(String tileValue) {
        return tileValue.startsWith("_") || tileValue.equals("0");
    }

    protected boolean isAccessible(String tileValue) {
        return tileValue.equals("1");
    }
}
