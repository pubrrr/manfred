package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.awt.*;
import java.util.HashMap;
import java.util.function.Function;

public class GelaberChoices extends AbstractGelaberText {
    HashMap<String, AbstractGelaberText> choices;

    private boolean showChoiceBox = false;
    private final SelectionMarker selectionMarker;
    private int selection;

    public GelaberChoices(String[] lines, HashMap<String, AbstractGelaberText> choices, SelectionMarker selectionMarker, GameConfig gameConfig) {
        super(gameConfig);
        this.lines = lines;
        this.choices = choices;
        this.selectionMarker = selectionMarker;
    }

    public HashMap<String, AbstractGelaberText> getChoices() {
        return choices;
    }

    @Override
    public Function<Gelaber, Function<GelaberController, ControllerInterface>> next() {
        boolean continueTalking = linesPosition + gameConfig.getNumberOfTextLines() - 1 < lines.length;

        if (continueTalking) {
            linesPosition += gameConfig.getNumberOfTextLines() - 1;
            return AbstractGelaberText::doNothing;
        } else if (!showChoiceBox) {
            showChoiceBox = true;
            return AbstractGelaberText::doNothing;
        }

        String selectedChoice = choices.keySet().toArray(new String[]{})[selection];
        AbstractGelaberText nextGelaber = choices.get(selectedChoice);

        resetSelection();

        if (nextGelaber == null) {
            return Gelaber::switchControlsBackToManfred;
        }
        return gelaber -> {
            gelaber.setCurrentText(nextGelaber);
            return ControllerInterface::self;
        };
    }

    private void resetSelection() {
        showChoiceBox = false;
        selectionMarker.resetToTop();
        linesPosition = 0;
        selection = 0;
    }

    @Override
    public void up() {
        if (showChoiceBox) {
            int initialSelection = selection;
            selection--;
            if (selection < 0) {
                selection = choices.size() - 1;
            }
            selectionMarker.translate(0, gameConfig.getSelectionMovementDistance() * (selection - initialSelection));
        }
    }

    @Override
    public void down() {
        if (showChoiceBox) {
            int initialSelection = selection;
            selection++;
            if (selection >= choices.size()) {
                selection = 0;
            }
            selectionMarker.translate(0, gameConfig.getSelectionMovementDistance() * (selection - initialSelection));
        }
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        super.paint(g, offset, x, y);

        if (showChoiceBox) {
            g.setColor(Color.YELLOW);
            g.fillRect(gameConfig.getGelaberBoxPositionX(), gameConfig.getGelaberBoxPositionY(), 500, 500);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

            selectionMarker.paint(g);

            g.setColor(Color.BLACK);
            int idx = 0;
            for (String choice : choices.keySet()) {
                g.drawString(
                        choice,
                        gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox(),
                        gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + idx++ * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2);
            }
        }
    }
}
