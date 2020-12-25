package manfred.game.interact.person;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.MoreCollectors;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class GelaberFacade implements Paintable {
    private final GelaberGraphMatrix gelaberGraphMatrix;
    private final ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> nodesByIdentifier;
    private final List<TextLineFactory> textLineFactories;

    private TextLine currentTextLine;

    public GelaberFacade(
        GelaberGraphMatrix gelaberGraphMatrix,
        ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> nodesByIdentifier,
        List<TextLineFactory> textLineFactories,
        GelaberNodeIdentifier initialGelaberNodeIdentifier
    ) {
        this.gelaberGraphMatrix = gelaberGraphMatrix;
        this.nodesByIdentifier = nodesByIdentifier;
        this.textLineFactories = textLineFactories;
        this.currentTextLine = buildTextLine(initialGelaberNodeIdentifier);
    }

    public Function<GelaberController, ControllerInterface> next() {
        GelaberResponseWrapper response = currentTextLine.next(this::buildTextLine);
        this.currentTextLine = response.getNextTextLine();
        return response.getContinueCommand();
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

    private TextLine buildTextLine(GelaberNodeIdentifier identifier) {
        GelaberNode gelaberNode = nodesByIdentifier.get(identifier);
        List<GelaberEdge> outgoingEdges = gelaberGraphMatrix.getOutgoingEdgesFor(identifier);

        return textLineFactories.stream()
            .filter(textLineFactory -> textLineFactory.appliesTo(outgoingEdges))
            .collect(MoreCollectors.onlyElement())
            .create(gelaberNode, outgoingEdges);
    }
}
