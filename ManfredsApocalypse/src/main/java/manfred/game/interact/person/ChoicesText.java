package manfred.game.interact.person;

import manfred.game.GameConfig;
import manfred.game.controls.ControllerInterface;

import java.util.function.Function;

public class ChoicesText extends BasicTextLine implements TextLine {
    private final ChoicesFacade choicesFacade;

    public ChoicesText(String[] textLines, GameConfig gameConfig, ChoicesFacade choicesFacade) {
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
        return new GelaberResponseWrapper(
            new ChoicesBox(this, this.choicesFacade),
            ControllerInterface::self
        );
    }
}
