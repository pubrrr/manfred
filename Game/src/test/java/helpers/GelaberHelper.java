package helpers;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GelaberHelper {

    public static GelaberPrototype setupGelaber(Map<String, List<String>> gelaberToReferencesMap, String initialReference) throws InvalidInputException {
        GelaberDto input = new GelaberDto();
        input.setInitialTextReference(initialReference);
        input.setTexts(
            gelaberToReferencesMap.entrySet().stream()
                .map(entry -> {
                    List<ReferenceDto> referenceDtos = entry.getValue().stream()
                        .map(reference -> {
                            ReferenceDto referenceDto = new ReferenceDto();
                            referenceDto.setTo(reference);
                            return referenceDto;
                        })
                        .collect(Collectors.toList());

                    GelaberTextDto gelaberTextDto = new GelaberTextDto();
                    gelaberTextDto.setReferences(referenceDtos);
                    gelaberTextDto.setText("some text for " + entry.getKey());
                    return Map.entry(entry.getKey(), gelaberTextDto);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );

        GelaberValidator gelaberValidator = new GelaberValidator();
        return gelaberValidator.validate(input);
    }
}
