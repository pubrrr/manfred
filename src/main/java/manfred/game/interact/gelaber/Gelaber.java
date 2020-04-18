package manfred.game.interact.gelaber;

import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Gelaber implements Paintable {
    public final static int TEXT_POINT_SIZE = 30;

    public final static int TEXT_BOX_DISTANCE_TO_BORDER = 100;
    public final static int TEXT_DISTANCE_TO_BOX = 50;
    public final static int TEXT_BOX_WIDTH = GamePanel.WIDTH - 2 * TEXT_BOX_DISTANCE_TO_BORDER;
    public final static int TEXT_BOX_HEIGHT = GamePanel.HEIGHT / 3 - TEXT_BOX_DISTANCE_TO_BORDER;

    public final static int TEXT_BOX_POSITION_X = TEXT_BOX_DISTANCE_TO_BORDER;
    public final static int TEXT_BOX_POSITION_Y = GamePanel.HEIGHT * 2 / 3;

    private GelaberText[] texts;

    public Gelaber(GelaberText[] textLines) {
        this.texts = textLines;
    }

    public GelaberText[] getTexts() {
        return texts;
    }

    private GelaberText currentText() {
        return texts[0];
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(TEXT_BOX_POSITION_X, TEXT_BOX_POSITION_Y, TEXT_BOX_WIDTH, TEXT_BOX_HEIGHT);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, TEXT_POINT_SIZE));
        for (String line: currentText().getLines()) {
            g.drawString(line, TEXT_BOX_POSITION_X + TEXT_DISTANCE_TO_BOX, TEXT_BOX_POSITION_Y + TEXT_DISTANCE_TO_BOX);
        }
    }
}
