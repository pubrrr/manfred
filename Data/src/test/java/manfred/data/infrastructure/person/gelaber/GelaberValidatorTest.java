package manfred.data.infrastructure.person.gelaber;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class GelaberValidatorTest {

    private GelaberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new GelaberValidator();
    }

    @Test
    void emptyTexts() {
        GelaberDto input = setupGelaber(Map.of(), "initial");

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Did not find initial reference"));
    }

    @Test
    void initialReferenceNotFound() {
        GelaberDto input = setupGelaber(
            Map.of("key", List.of(""), "key2", List.of("")),
            "initial"
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Did not find initial reference initial in references [key2, key]"));
    }

    @Test
    void emptyReferences() {
        GelaberDto input = setupGelaber(
            Map.of("initial", List.of()),
            "initial"
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Found empty references"));
    }

    @Test
    void textHasUnknownReference() {
        GelaberDto input = setupGelaber(
            Map.of("initial", List.of("unknownReference")),
            "initial"
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Found unknown references: unknownReference"));
    }

    @Test
    void textHasUnknownAndKnownReference() {
        GelaberDto input = setupGelaber(
            Map.of("initialReference", List.of("initialReference", "unknownReference")),
            "initialReference"
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Found unknown references: unknownReference"));
    }

    @Test
    void textHasTwoUnknownReferences() {
        GelaberDto input = setupGelaber(
            Map.of("initialReference", List.of("unknown1", "unknown2")),
            "initialReference"
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Found unknown references: unknown1, unknown2"));
    }

    @Test
    void oneSelfReferencingText() throws InvalidInputException {
        GelaberDto input = setupGelaber(
            Map.of("initialReference", List.of("initialReference")),
            "initialReference"
        );

        GelaberPrototype result = underTest.validate(input);

        assertThat(result.getInitialReference().getId(), is("initialReference"));
        assertThat(result.getTextIds(), hasSize(1));
        List<ReferencePrototype> references = result.getGelaber(result.getInitialReference()).getReferences();
        assertThat(references, hasSize(1));
        assertThat(references.get(0).getReferencedId().getId(), is("initialReference"));
    }

    @Test
    void twoTextsReferencingEachOther() throws InvalidInputException {
        GelaberDto input = setupGelaber(
            Map.of(
                "initialReference", List.of("otherReference"),
                "otherReference", List.of("initialReference")
            ),
            "initialReference"
        );

        GelaberPrototype result = underTest.validate(input);

        assertThat(result.getInitialReference().getId(), is("initialReference"));
        assertThat(result.getTextIds(), hasSize(2));
        GelaberPrototype.TextId firstReference = result.getGelaber(result.getInitialReference()).getReferences().get(0).getReferencedId();
        GelaberPrototype.TextId secondReference = result.getGelaber(firstReference).getReferences().get(0).getReferencedId();
        assertThat(secondReference, is(result.getInitialReference()));
    }

    @Test
    void textWithTwoValidReferences() throws InvalidInputException {
        GelaberDto input = setupGelaber(
            Map.of(
                "initialReference", List.of("initialReference", "otherReference"),
                "otherReference", List.of("initialReference")
            ),
            "initialReference"
        );

        GelaberPrototype result = underTest.validate(input);

        List<ReferencePrototype> firstReferences = result.getGelaber(result.getInitialReference()).getReferences();
        assertThat(firstReferences, hasSize(2));
        List<ReferencePrototype> secondReferences = result.getGelaber(firstReferences.get(1).getReferencedId()).getReferences();
        assertThat(secondReferences, hasSize(1));
    }

    @Test
    void textWithTwiceTheSameValidReferences() throws InvalidInputException {
        GelaberDto input = setupGelaber(
            Map.of(
                "initialReference", List.of("initialReference", "initialReference"),
                "otherReference", List.of("initialReference")
            ),
            "initialReference"
        );

        GelaberPrototype result = underTest.validate(input);

        List<ReferencePrototype> firstReferences = result.getGelaber(result.getInitialReference()).getReferences();
        assertThat(firstReferences, hasSize(2));
    }

    private GelaberDto setupGelaber(Map<String, List<String>> gelaberToReferencesMap, String initialReference) {
        GelaberDto input = new GelaberDto();

        Map<String, GelaberTextDto> texts = gelaberToReferencesMap.entrySet().stream()
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
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        input.setInitialTextReference(initialReference);
        input.setTexts(texts);
        return input;
    }
}