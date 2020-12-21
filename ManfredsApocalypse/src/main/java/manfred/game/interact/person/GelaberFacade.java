package manfred.game.interact.person;

import com.google.common.collect.ImmutableBiMap;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.function.Function;

public class GelaberFacade implements Paintable {
    private final GelaberGraphMatrix gelaberGraphMatrix;
    private final ImmutableBiMap<Selection, TextLine> textLines;
    private TextLine currentTextLine;

    public GelaberFacade(GelaberGraphMatrix gelaberGraphMatrix, ImmutableBiMap<Selection, TextLine> textLines, Selection initialSelection) {
        this.gelaberGraphMatrix = gelaberGraphMatrix;
        this.textLines = textLines;
        this.currentTextLine = textLines.get(initialSelection);
    }

    public Function<GelaberController, ControllerInterface> next() {
        ReferencingTextLineWrapper nextTextLineWrapper = currentTextLine.next();
        this.currentTextLine = textLines.get(nextTextLineWrapper.getNext())
            .withTransitions(gelaberGraphMatrix.getTransitionsFor(nextTextLineWrapper.getNext()));
        return nextTextLineWrapper.getContinueCommand();
    }

    public void down() {
        currentTextLine.down();
    }

    public void up() {
        currentTextLine.up();
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        currentTextLine.paint(g, offset, x, y);
    }
}
