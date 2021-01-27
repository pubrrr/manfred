package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;

import java.awt.*;
import java.util.List;

public abstract class BasicTextLine implements TextLine {
    private final List<String> textLines;
    protected final GameConfig gameConfig;

    protected BasicTextLine(List<String> textLines, GameConfig gameConfig) {
        this.textLines = textLines;
        this.gameConfig = gameConfig;
    }

    @Override
    public final void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(gameConfig.getTextBoxPositionX(), gameConfig.getTextBoxPositionY(), gameConfig.getTextBoxWidth(), gameConfig.getTextBoxHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

        for (int idx = 0; idx < gameConfig.getNumberOfTextLines(); idx++) {
            g.drawString(
                idx < textLines.size() ? textLines.get(idx) : "",
                gameConfig.getTextBoxPositionX() + gameConfig.getTextDistanceToBox(),
                gameConfig.getTextBoxPositionY() + gameConfig.getTextDistanceToBox() + idx * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2
            );
        }
    }
}
