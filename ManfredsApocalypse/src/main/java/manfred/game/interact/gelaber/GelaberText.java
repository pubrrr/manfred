package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

public class GelaberText extends AbstractGelaberText {
    public GelaberText(String[] lines, GameConfig gameConfig) {
        super(gameConfig);
        this.lines = lines;
    }

    @Override
    public Function<Gelaber, Function<GelaberController, ControllerInterface>> next() {
        linesPosition += gameConfig.getNumberOfTextLines() - 1;
        boolean continueTalking = linesPosition < lines.length;

        if (continueTalking) {
            return AbstractGelaberText::doNothing;
        }

        linesPosition = 0;
        return Gelaber::switchControlsBackToManfred;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }
}

