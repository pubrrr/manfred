package manfred.game.interact.gelaber;

public class TextLine {
    private String text;
    private GelaberType type;

    public TextLine(String text, GelaberType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public GelaberType getType() {
        return this.type;
    }
}
