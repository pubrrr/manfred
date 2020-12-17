package manfred.game.interact.gelaber;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.function.Function;

public class Gelaber implements Paintable {
    private final AbstractGelaberText[] texts;

    private AbstractGelaberText currentText;
    private int nextTextPointer = 0;

    public Gelaber(AbstractGelaberText[] texts) {
        this.texts = texts;
        currentText = texts[0];
    }

    public AbstractGelaberText[] getTexts() {
        return texts;
    }

    public Function<GelaberController, ControllerInterface> next() {
        Function<Gelaber, Function<GelaberController, ControllerInterface>> callback = currentText.next();
        return callback.apply(this);
    }

    void setCurrentText(AbstractGelaberText currentText) {
        this.currentText = currentText;
    }

    public Function<GelaberController, ControllerInterface> switchControlsBackToManfred() {
        if (nextTextPointer < texts.length - 1) {
            nextTextPointer++;
        }
        currentText = texts[nextTextPointer];

        return GelaberController::previous;
    }

    public void down() {
        currentText.down();
    }

    public void up() {
        currentText.up();
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        currentText.paint(g, offset, x, y);
    }
}
