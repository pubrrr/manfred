package manfred.game.interact.gelaber;

import java.awt.*;
import java.util.HashMap;

public class GelaberChoices extends AbstractGelaberText {
    HashMap<String, AbstractGelaberText> choices;

    private boolean showChoiceBox = false;

    public GelaberChoices(String[] lines, HashMap<String, AbstractGelaberText> choices) {
        this.lines = lines;
        this.choices = choices;
    }

    public HashMap<String, AbstractGelaberText> getChoices() {
        return choices;
    }

    @Override
    public boolean next() {
        boolean superFoundNextLine = super.next();
        if (superFoundNextLine) {
            return true;
        } else if (!showChoiceBox) {
            showChoiceBox = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (showChoiceBox) {
            g.setColor(Color.YELLOW);
            g.fillRect(Gelaber.GELABER_BOX_POSITION_X, Gelaber.GELABER_BOX_POSITION_Y, 500, 500);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Palatino Linotype", Font.BOLD, Gelaber.TEXT_POINT_SIZE));

            int idx = 0;
            for (String choice : choices.keySet()) {
                g.drawString(
                        choice,
                        Gelaber.GELABER_BOX_POSITION_X + Gelaber.TEXT_DISTANCE_TO_BOX,
                        Gelaber.GELABER_BOX_POSITION_Y + Gelaber.TEXT_DISTANCE_TO_BOX + idx++ * (Gelaber.TEXT_POINT_SIZE + Gelaber.DISTANCE_BETWEEN_LINES) + Gelaber.TEXT_POINT_SIZE / 2);
            }
        }
    }
}
