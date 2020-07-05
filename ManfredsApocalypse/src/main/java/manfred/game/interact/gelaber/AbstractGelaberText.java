package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

abstract public class AbstractGelaberText {
    protected String[] lines;

    protected int linesPosition = 0;
    protected GameConfig gameConfig;

    public String[] getLines() {
        return this.lines;
    }

    public AbstractGelaberText(GameConfig gameConfig){
        this.gameConfig = gameConfig;
    }

    public void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(gameConfig.getTextBoxPositionX(), gameConfig.getTextBoxPositionY(), gameConfig.getTextBoxWidth(), gameConfig.getTextBoxHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

        for (int idx = 0; idx < gameConfig.getNumberOfTextLines(); idx++) {
            g.drawString(
                    linesPosition + idx < lines.length ? lines[linesPosition + idx] : "",
                    gameConfig.getTextBoxPositionX() + gameConfig.getTextDistanceToBox(),
                    gameConfig.getTextBoxPositionY() + gameConfig.getTextDistanceToBox() + idx * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2
            );
        }
    }

    public abstract Function<Gelaber, Consumer<KeyControls>> next();

    public abstract void up();

    public abstract void down();
}
