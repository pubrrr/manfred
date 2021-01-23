package manfred.game.interact.person.gelaber;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
public class GelaberConverter {

    private final GelaberFacadeBuilder gelaberFacadeBuilder;
    private final LineSplitter lineSplitter;

    public GelaberConverter(GelaberFacadeBuilder gelaberFacadeBuilder, LineSplitter lineSplitter) {
        this.gelaberFacadeBuilder = gelaberFacadeBuilder;
        this.lineSplitter = lineSplitter;
    }

    public GelaberFacade convert(GelaberDto gelaber) throws InvalidInputException {
        ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap = gelaber.getTexts().keySet().stream()
            .collect(collectingAndThen(toMap(Function.identity(), GelaberNodeIdentifier::new), ImmutableMap::copyOf));

        checkReferences(keyToIdMap, gelaber);

        ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes = mapDtosToTextLines(gelaber.getTexts());

        GelaberGraphMatrix gelaberGraphMatrix = new GelaberGraphMatrix(
            gelaber.getTexts().entrySet().stream()
                .map(entry -> mapKeyToSelection(entry, keyToIdMap))
                .map(entry -> mapValueToGelaberEdges(entry, keyToIdMap))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        );

        return gelaberFacadeBuilder
            .withNodes(gelaberNodes)
            .withGraphMatrix(gelaberGraphMatrix)
            .buildStartingAt(keyToIdMap.get(gelaber.getInitialTextReference()));
    }

    private void checkReferences(ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap, GelaberDto gelaber) throws InvalidInputException {
        if (!keyToIdMap.containsKey(gelaber.getInitialTextReference())) {
            throw new InvalidInputException("Unknown initial gelaber reference " + gelaber.getInitialTextReference() + " not found in " + keyToIdMap.keySet());
        }

        Optional<ReferenceDto> unknownReference = gelaber.getTexts().values().stream()
            .flatMap(gelaberTextDto -> gelaberTextDto.getReferences().stream())
            .filter(referenceDto -> !keyToIdMap.containsKey(referenceDto.getTo()))
            .findAny();

        if (unknownReference.isPresent()) {
            throw new InvalidInputException("Unknown gelaber reference: " + unknownReference.get().getTo() + " not found in " + keyToIdMap.keySet());
        }
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

    private Map.Entry<GelaberNodeIdentifier, List<GelaberEdge>> mapValueToGelaberEdges(Map.Entry<GelaberNodeIdentifier, GelaberTextDto> entry, ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap) {
        return Map.entry(
            entry.getKey(),
            entry.getValue().getReferences().stream()
                .map(referenceDto -> mapReferenceToGelaberEdge(referenceDto, keyToIdMap))
                .collect(Collectors.toList())
        );
    }

    private GelaberEdge mapReferenceToGelaberEdge(ReferenceDto referenceDto, ImmutableMap<String, GelaberNodeIdentifier> keyToIdMap) {
        return referenceDto.isContinueTalking()
            ? GelaberEdge.continuingWith(keyToIdMap.get(referenceDto.getTo()), referenceDto.getText())
            : GelaberEdge.abortingReferencingTo(keyToIdMap.get(referenceDto.getTo()), referenceDto.getText());
    }
}
