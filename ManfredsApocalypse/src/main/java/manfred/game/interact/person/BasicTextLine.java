package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.awt.*;

public abstract class BasicTextLine implements TextLine {
    private final String[] textLines;
    protected final GameConfig gameConfig;

    protected BasicTextLine(String[] textLines, GameConfig gameConfig) {
        this.textLines = textLines;
        this.gameConfig = gameConfig;
    }

    @Override
    public final void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.YELLOW);
        g.fillRect(gameConfig.getTextBoxPositionX(), gameConfig.getTextBoxPositionY(), gameConfig.getTextBoxWidth(), gameConfig.getTextBoxHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

        for (int idx = 0; idx < gameConfig.getNumberOfTextLines(); idx++) {
            g.drawString(
                idx < textLines.length ? textLines[idx] : "",
                gameConfig.getTextBoxPositionX() + gameConfig.getTextDistanceToBox(),
                gameConfig.getTextBoxPositionY() + gameConfig.getTextDistanceToBox() + idx * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2
            );
        }
    }
}
