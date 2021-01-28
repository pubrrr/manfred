package manfred.game.interact.person.gelaber;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.Paintable;
import manfred.game.interact.person.textLineFactory.TextLineFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class GelaberFacade implements Paintable {
    private final GelaberGraphMatrix gelaberGraphMatrix;
    private final Map<GelaberNodeIdentifier, GelaberNode> nodesByIdentifier;
    private final TextLineFactory textLineFactory;

    private TextLine currentTextLine;

    public GelaberFacade(
        GelaberGraphMatrix gelaberGraphMatrix,
        Map<GelaberNodeIdentifier, GelaberNode> nodesByIdentifier,
        TextLineFactory textLineFactory,
        GelaberNodeIdentifier initialGelaberNodeIdentifier
    ) {
        this.gelaberGraphMatrix = gelaberGraphMatrix;
        this.nodesByIdentifier = nodesByIdentifier;
        this.textLineFactory = textLineFactory;
        this.currentTextLine = buildTextLine(initialGelaberNodeIdentifier);
    }

    public ControllerStateMapper<GelaberController, ControllerInterface> next() {
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
    public void paint(Graphics g) {
        currentTextLine.paint(g);
    }

    private TextLine buildTextLine(GelaberNodeIdentifier identifier) {
        GelaberNode gelaberNode = nodesByIdentifier.get(identifier);
        List<GelaberEdge> outgoingEdges = gelaberGraphMatrix.getOutgoingEdgesFor(identifier);

        return textLineFactory.create(gelaberNode, outgoingEdges);
    }
}
