package manfred.game.interact.gelaber;

import manfred.game.controls.KeyControls;

import java.util.function.Consumer;
import java.util.function.Function;

public class GelaberText extends AbstractGelaberText {
    public GelaberText(String[] lines) {
        this.lines = lines;
    }

    @Override
    public Function<Gelaber, Consumer<KeyControls>> next() {
        linesPosition += Gelaber.NUMBER_OF_TEXT_LINES - 1;
        boolean continueTalking = linesPosition < lines.length;

        if (continueTalking) {
            return gelaber -> null;
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

