package manfred.game.interact.gelaber;

import java.awt.*;

abstract public class AbstractGelaberText {
    protected String[] lines;

    protected int linesPosition = 0;

    public String[] getLines() {
        return this.lines;
    }

    public void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(Gelaber.TEXT_BOX_POSITION_X, Gelaber.TEXT_BOX_POSITION_Y, Gelaber.TEXT_BOX_WIDTH, Gelaber.TEXT_BOX_HEIGHT);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, Gelaber.TEXT_POINT_SIZE));

        for (int idx = 0; idx < Gelaber.NUMBER_OF_TEXT_LINES; idx++) {
            g.drawString(
                    linesPosition + idx < lines.length ? lines[linesPosition + idx] : "",
                    Gelaber.TEXT_BOX_POSITION_X + Gelaber.TEXT_DISTANCE_TO_BOX,
                    Gelaber.TEXT_BOX_POSITION_Y + Gelaber.TEXT_DISTANCE_TO_BOX + idx * (Gelaber.TEXT_POINT_SIZE + Gelaber.DISTANCE_BETWEEN_LINES) + Gelaber.TEXT_POINT_SIZE / 2
            );
        }
    }

    public boolean next() {
        linesPosition += Gelaber.NUMBER_OF_TEXT_LINES - 1;
        return linesPosition < lines.length;
    }

    public abstract void up();

    public abstract void down();
}
