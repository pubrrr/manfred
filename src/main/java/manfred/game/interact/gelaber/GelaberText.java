package manfred.game.interact.gelaber;

public class GelaberText extends AbstractGelaberText {
    public GelaberText(String[] lines) {
        this.lines = lines;
    }

    @Override
    public GelaberNextResponse next() {
        linesPosition += Gelaber.NUMBER_OF_TEXT_LINES - 1;
        boolean continueTalking = linesPosition < lines.length;
        if (!continueTalking) {
            linesPosition = 0;
        }
        return new GelaberNextResponse(continueTalking);
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }
}

