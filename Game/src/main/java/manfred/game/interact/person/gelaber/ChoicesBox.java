package manfred.game.interact.person.gelaber;

import java.awt.*;
import java.util.function.Function;

public class ChoicesBox implements TextLine {
    private final ChoicesText choicesText;
    private final ChoicesFacade choicesFacade;

    public ChoicesBox(ChoicesText choicesText, ChoicesFacade choicesFacade) {
        this.choicesText = choicesText;
        this.choicesFacade = choicesFacade;
    }

    @Override
    public void up() {
        choicesFacade.up();
    }

    @Override
    public void down() {
        choicesFacade.down();
    }

    @Override
    public GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier) {
        GelaberEdge edge = choicesFacade.confirm();
        return GelaberResponseWrapper.followingTheEdge(edge, successorSupplier);
    }

    @Override
    public void paint(Graphics g) {
        choicesText.paint(g);

        choicesFacade.paint(g);
    }
}
