package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.List;
import java.util.function.Function;

public class TextLineWrapper extends BasicTextLine {
    private final TextLine wrapped;

    protected TextLineWrapper(List<String> textLines, GameConfig gameConfig, TextLine wrapped) {
        super(textLines, gameConfig);
        this.wrapped = wrapped;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier) {
        return GelaberResponseWrapper.continuingWith(wrapped);
    }
}
