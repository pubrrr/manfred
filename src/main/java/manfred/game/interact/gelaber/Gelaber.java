package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;
import manfred.game.graphics.Paintable;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Gelaber implements Paintable {
    private AbstractGelaberText[] texts;
    private GameConfig gameConfig;

    private AbstractGelaberText currentText;
    private int nextTextPointer = 0;

    public Gelaber(AbstractGelaberText[] texts, GameConfig gameConfig) {
        this.texts = texts;
        currentText = texts[0];
        this.gameConfig = gameConfig;
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
            keyControls.getGamePanel().deleteGelaber();
        };
    }

    public void down() {
        currentText.down();
    }

    public void up() {
        currentText.up();
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        currentText.paint(g);
    }
}
