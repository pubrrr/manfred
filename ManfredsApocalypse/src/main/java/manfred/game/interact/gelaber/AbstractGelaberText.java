package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.function.Function;

abstract public class AbstractGelaberText implements Paintable {
    protected String[] lines;

    protected int linesPosition = 0;
    protected GameConfig gameConfig;

    public String[] getLines() {
        return this.lines;
    }

    public AbstractGelaberText(GameConfig gameConfig){
        this.gameConfig = gameConfig;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
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

    public abstract Function<Gelaber, Function<GelaberController, ControllerInterface>> next();

    public abstract void up();

    public abstract void down();

    public static Function<GelaberController, ControllerInterface> doNothing(Gelaber gelaber) {
        return ControllerInterface::self;
    }
}
