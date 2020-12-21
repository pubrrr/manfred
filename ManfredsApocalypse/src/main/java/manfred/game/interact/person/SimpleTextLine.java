package manfred.game.interact.person;

import manfred.game.GameConfig;

public class SimpleTextLine extends BasicTextLine implements TextLine {
    private final GelaberEdge nextTextLineWrapper;

    public SimpleTextLine(String[] textLines, GameConfig gameConfig, GelaberEdge nextTextLineWrapper) {
        super(textLines, gameConfig);
        this.nextTextLineWrapper = nextTextLineWrapper;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public GelaberEdge next() {
        return nextTextLineWrapper;
    }
}
