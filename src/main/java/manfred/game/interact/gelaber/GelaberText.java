package manfred.game.interact.gelaber;

public class GelaberText {
    private String[] lines;
    private GelaberType type;

    public GelaberText(String[] lines, GelaberType type) {
        this.lines = lines;
        this.type = type;
    }

    public String[] getLines() {
        return this.lines;
    }

    public GelaberType getType() {
        return this.type;
    }
}
