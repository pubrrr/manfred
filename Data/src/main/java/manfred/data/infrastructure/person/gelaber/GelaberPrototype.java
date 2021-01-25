package manfred.data.infrastructure.person.gelaber;

import lombok.Getter;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GelaberPrototype {

    private final TextId initialReference;
    private final Map<TextId, GelaberTextPrototype> textPrototypes;

    private GelaberPrototype(TextId initialReference, Map<TextId, GelaberTextPrototype> textPrototypes) {
        this.initialReference = initialReference;
        this.textPrototypes = textPrototypes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public TextId getInitialReference() {
        return initialReference;
    }

    public Collection<TextId> getTextIds() {
        return textPrototypes.keySet();
    }

    public GelaberTextPrototype getGelaber(TextId id) {
        return textPrototypes.get(id);
    }

    public <K, V> Map<K, V> mapTexts(Function<TextId, K> keyMap, Function<GelaberTextPrototype, V> valueMap) {
        return textPrototypes.entrySet().stream().collect(toMap(
            textPrototypeById -> keyMap.apply(textPrototypeById.getKey()),
            textPrototypeById -> valueMap.apply(textPrototypeById.getValue())
        ));
    }

    @Getter
    public static class TextId {
        private final String id;

        // make sure these ids are only created upon creation of the prototype, thus no unknown ids can appear
        private TextId(String id) {
            this.id = id;
        }
    }

    public static class Builder {
        private String initialReference;
        private Map<String, GelaberTextDto> textsByKey;

        public Builder withInitialReference(String initialReference) {
            this.initialReference = initialReference;
            return this;
        }

        public Builder WithTexts(Map<String, GelaberTextDto> textsByKey) {
            this.textsByKey = textsByKey;
            return this;
        }

        public GelaberPrototype validateAndBuild() throws InvalidInputException {
            requireNonNull(initialReference);
            requireNonNull(textsByKey);

            Map<String, TextId> keyToIdMap = textsByKey.keySet().stream().collect(toMap(string -> string, TextId::new));

            validateReferencesAreNotEmpty();
            validateInitialReferenceIsKnown(keyToIdMap);
            validateAllReferencesAreKnown(keyToIdMap);

            Map<TextId, GelaberTextPrototype> textPrototypes = textsByKey.entrySet().stream()
                .map(gelaberTextDtoByKey -> Map.entry(
                    keyToIdMap.get(gelaberTextDtoByKey.getKey()),
                    toGelaberTextPrototype(keyToIdMap, gelaberTextDtoByKey)
                ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new GelaberPrototype(
                keyToIdMap.get(initialReference),
                textPrototypes
            );
        }

        private GelaberTextPrototype toGelaberTextPrototype(Map<String, TextId> keyToIdMap, Map.Entry<String, GelaberTextDto> gelaberTextDtoByKey) {
            GelaberTextDto gelaberTextDto = gelaberTextDtoByKey.getValue();
            return new GelaberTextPrototype(
                gelaberTextDto.getText(),
                toReferencePrototypes(gelaberTextDto.getReferences(), keyToIdMap)
            );
        }

        private List<ReferencePrototype> toReferencePrototypes(List<ReferenceDto> references, Map<String, TextId> keyToIdMap) {
            return references.stream()
                .map(referenceDto -> new ReferencePrototype(keyToIdMap.get(referenceDto.getTo()), referenceDto.getText(), referenceDto.isContinueTalking()))
                .collect(toList());
        }

        private void validateReferencesAreNotEmpty() throws InvalidInputException {
            Optional<List<ReferenceDto>> emptyReference = textsByKey.values().stream()
                .map(GelaberTextDto::getReferences)
                .filter(List::isEmpty)
                .findFirst();
            if (emptyReference.isPresent()) {
                throw new InvalidInputException("Found empty references");
            }
        }

        private void validateInitialReferenceIsKnown(Map<String, TextId> keyToIdMap) throws InvalidInputException {
            if (!keyToIdMap.containsKey(initialReference)) {
                throw new InvalidInputException("Did not find initial reference initial in references " + keyToIdMap.keySet().toString());
            }
        }

        private void validateAllReferencesAreKnown(Map<String, TextId> keyToIdMap) throws InvalidInputException {
            List<String> unknownReferences = textsByKey.values().stream()
                .flatMap(gelaberTextDto -> gelaberTextDto.getReferences().stream())
                .map(ReferenceDto::getTo)
                .filter(key -> !keyToIdMap.containsKey(key))
                .collect(toList());
            if (!unknownReferences.isEmpty()) {
                throw new InvalidInputException("Found unknown references: " + String.join(", ", unknownReferences));
            }
        }
    }
}
