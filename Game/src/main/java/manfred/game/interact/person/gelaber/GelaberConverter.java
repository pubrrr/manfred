package manfred.game.interact.person.gelaber;

import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.infrastructure.person.gelaber.GelaberTextPrototype;
import manfred.data.infrastructure.person.gelaber.ReferencePrototype;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;

@Component
public class GelaberConverter {

    private final GelaberFacadeBuilder gelaberFacadeBuilder;
    private final LineSplitter lineSplitter;

    public GelaberConverter(GelaberFacadeBuilder gelaberFacadeBuilder, LineSplitter lineSplitter) {
        this.gelaberFacadeBuilder = gelaberFacadeBuilder;
        this.lineSplitter = lineSplitter;
    }

    public GelaberFacade convert(GelaberPrototype gelaberPrototype) {
        Map<GelaberPrototype.TextId, GelaberNodeIdentifier> idToNodeMap = gelaberPrototype.getTextIds().stream().collect(toMap(
            textId -> textId,
            GelaberNodeIdentifier::of
        ));

        Map<GelaberNodeIdentifier, GelaberNode> gelaberNodes = gelaberPrototype.mapTexts(
            idToNodeMap::get,
            this::mapToTextLine
        );

        GelaberGraphMatrix gelaberGraphMatrix = new GelaberGraphMatrix(
            gelaberPrototype.mapTexts(
                idToNodeMap::get,
                gelaberTextPrototype -> mapToGelaberEdges(gelaberTextPrototype.getReferences(), idToNodeMap)
            )
        );

        return gelaberFacadeBuilder
            .withNodes(gelaberNodes)
            .withGraphMatrix(gelaberGraphMatrix)
            .buildStartingAt(idToNodeMap.get(gelaberPrototype.getInitialReference()));
    }

    private GelaberNode mapToTextLine(GelaberTextPrototype gelaberTextDto) {
        return new GelaberNode(lineSplitter.splitIntoTextLinesFittingIntoTextBox(gelaberTextDto.getText()));
    }

    private List<GelaberEdge> mapToGelaberEdges(List<ReferencePrototype> gelaberTextPrototype, Map<GelaberPrototype.TextId, GelaberNodeIdentifier> idToNodeMap) {
        return gelaberTextPrototype.stream()
            .map(referencePrototype -> mapReferenceToGelaberEdge(referencePrototype, idToNodeMap))
            .collect(toList());
    }

    private GelaberEdge mapReferenceToGelaberEdge(ReferencePrototype referencePrototype, Map<GelaberPrototype.TextId, GelaberNodeIdentifier> idToNodeMap) {
        GelaberNodeIdentifier nodeIdentifier = idToNodeMap.get(referencePrototype.getReferencedId());

        return referencePrototype.isContinueTalking()
            ? GelaberEdge.continuingWith(nodeIdentifier, referencePrototype.getEdgeText())
            : GelaberEdge.abortingReferencingTo(nodeIdentifier, referencePrototype.getEdgeText());
    }
}
