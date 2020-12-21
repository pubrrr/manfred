package manfred.game.infrastructure.person;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import manfred.game.interact.person.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Component
public class GelaberConverter {

    private final GelaberFacadeBuilder gelaberFacadeBuilder;
    private final LineSplitter lineSplitter;

    public GelaberConverter(GelaberFacadeBuilder gelaberFacadeBuilder, LineSplitter lineSplitter) {
        this.gelaberFacadeBuilder = gelaberFacadeBuilder;
        this.lineSplitter = lineSplitter;
    }

    public GelaberFacade convert(GelaberDto gelaber) {
        ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap = gelaber.getTexts().keySet().stream()
            .collect(collectingAndThen(toMap(Function.identity(), GelaberNodeIdentifier::new), ImmutableMap::copyOf));

        ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes = mapDtosToTextLines(gelaber.getTexts());

        GelaberGraphMatrix gelaberGraphMatrix = new GelaberGraphMatrix(
            gelaber.getTexts().entrySet().stream()
                .map(entry -> mapKeyToSelection(entry, keyToIdMap))
                .map(entry -> mapValueToGelaberNode(entry, keyToIdMap))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        );

        return gelaberFacadeBuilder
            .withNodes(gelaberNodes)
            .withGraphMatrix(gelaberGraphMatrix)
            .buildStartingAt(keyToIdMap.get(gelaber.getInitialTextReference()));
    }

    private ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> mapDtosToTextLines(Map<String, GelaberTextDto> texts) {
        return texts.entrySet().stream()
            .map(entry -> Map.entry(new GelaberNodeIdentifier(entry.getKey()), mapToTextLine(entry.getValue())))
            .collect(collectingAndThen(toMap(Map.Entry::getKey, Map.Entry::getValue), ImmutableBiMap::copyOf));
    }

    private GelaberNode mapToTextLine(GelaberTextDto gelaberTextDto) {
        return new GelaberNode(lineSplitter.splitIntoTextLinesFittingIntoTextBox(gelaberTextDto.getText()));
    }

    private Map.Entry<GelaberNodeIdentifier, GelaberTextDto> mapKeyToSelection(Map.Entry<String, GelaberTextDto> entry, ImmutableMap<String, GelaberNodeIdentifier> keyToSelectionMap) {
        return Map.entry(keyToSelectionMap.get(entry.getKey()), entry.getValue());
    }

    private Map.Entry<GelaberNodeIdentifier, List<ReferencingTextLineWrapper>> mapValueToGelaberNode(Map.Entry<GelaberNodeIdentifier, GelaberTextDto> entry, ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap) {
        return Map.entry(
            entry.getKey(),
            entry.getValue().getReferences().stream()
                .map(referenceDto -> mapReferenceToGelaberEdge(referenceDto, keyToIdMap))
                .collect(toList())
        );
    }

    private ReferencingTextLineWrapper mapReferenceToGelaberEdge(ReferenceDto referenceDto, ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap) {
        return referenceDto.isContinueTalking()
            ? GelaberEdge.continuingWith(keyToIdMap.get(referenceDto.getTo()))
            : GelaberEdge.abortingReferencingTo(keyToIdMap.get(referenceDto.getTo()));
    }
}
