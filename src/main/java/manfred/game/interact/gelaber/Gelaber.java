package manfred.game.interact.gelaber;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Gelaber implements Paintable {
    private AbstractGelaberText[] texts;
    public final static int TEXT_DISTANCE_TO_BOX = 50;
    public final static int TEXT_POINT_SIZE = 30;

    public final static int TEXT_BOX_DISTANCE_TO_BORDER = 100;
    public final static int TEXT_BOX_WIDTH = GamePanel.WIDTH - 2 * TEXT_BOX_DISTANCE_TO_BORDER;
    public final static int TEXT_BOX_HEIGHT = GamePanel.HEIGHT / 3 - TEXT_BOX_DISTANCE_TO_BORDER;

    public final static int TEXT_BOX_POSITION_X = TEXT_BOX_DISTANCE_TO_BORDER;
    public final static int TEXT_BOX_POSITION_Y = GamePanel.HEIGHT * 2 / 3;
    public final static int GELABER_BOX_POSITION_X = 100;
    public final static int GELABER_BOX_POSITION_Y = 100;

    public final static int DISTANCE_BETWEEN_LINES = 20;
    public final static int NUMBER_OF_TEXT_LINES = (TEXT_BOX_HEIGHT - 2 * TEXT_DISTANCE_TO_BOX) / (TEXT_POINT_SIZE + DISTANCE_BETWEEN_LINES);

    private AbstractGelaberText currentText;
    private int nextTextPointer = 0;

    public Gelaber(AbstractGelaberText[] texts) {
        this.texts = texts;
        currentText = texts[0];
    }

    public AbstractGelaberText[] getTexts() {
        return texts;
    }

    @Nullable
    public Consumer<KeyControls> next() {
        Function<Gelaber, Consumer<KeyControls>> callback = currentText.next();
        return callback.apply(this);
    }

    void setCurrentText(AbstractGelaberText currentText) {
        this.currentText = currentText;
    }

    Consumer<KeyControls> switchControlsBackToManfred() {
        if (nextTextPointer < texts.length - 1) {
            nextTextPointer++;
        }
        currentText = texts[nextTextPointer];

        return keyControls -> {
            keyControls.controlManfred();
            keyControls.getGamePanel().deletePaintable(this);
        };
    }

    public void down() {
        currentText.down();
    }

    public void up() {
        currentText.up();
    }

    @Override
    public void paint(Graphics g, Point offset) {
        currentText.paint(g);
    }
}
