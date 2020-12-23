package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.List;
import java.util.function.Function;

public class ChoicesText extends BasicTextLine implements TextLine {
    private final ChoicesFacade choicesFacade;

    public ChoicesText(List<String> textLines, GameConfig gameConfig, ChoicesFacade choicesFacade) {
        super(textLines, gameConfig);
        this.choicesFacade = choicesFacade;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier) {
        return GelaberResponseWrapper.continuingWith(new ChoicesBox(this, this.choicesFacade));
    }
}
