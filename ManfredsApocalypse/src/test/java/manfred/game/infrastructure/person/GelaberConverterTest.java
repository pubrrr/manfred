package manfred.game.infrastructure.person;

import manfred.game.exception.InvalidInputException;
import manfred.game.interact.person.GelaberFacade;
import manfred.game.interact.person.GelaberFacadeBuilder;
import manfred.game.interact.person.SimpleTextLine;
import manfred.game.interact.person.SimpleTextLineFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GelaberConverterTest {
    private GelaberConverter underTest;
    private SimpleTextLineFactory simpleTextLineFactoryMock;

    @BeforeEach
    void init() {
        simpleTextLineFactoryMock = mock(SimpleTextLineFactory.class);

        underTest = new GelaberConverter(
            new GelaberFacadeBuilder(List.of(simpleTextLineFactoryMock)),
            new LineSplitter(1)
        );
    }

    @Test
    void emptyGelaber() {
        GelaberDto input = setupGelaber(Map.of(), "unknown");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.convert(input));
        assertThat(exception.getMessage(), containsString("Unknown initial gelaber reference unknown not found"));
    }

    @Test
    void referenceNotFound() {
        GelaberDto input = setupGelaber(Map.of("key", List.of("unknown")), "key");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.convert(input));
        assertThat(exception.getMessage(), containsString("Unknown gelaber reference"));
    }

    @Test
    void happyPath() throws InvalidInputException {
        when(simpleTextLineFactoryMock.appliesTo(any())).thenReturn(true);
        when(simpleTextLineFactoryMock.create(any(), any())).thenReturn(mock(SimpleTextLine.class));

        GelaberDto input = setupGelaber(
            Map.of(
                "first", List.of("second"),
                "second", List.of("first", "second")
            ),
            "first"
        );

        GelaberFacade result = underTest.convert(input);

        assertThat(result, instanceOf(GelaberFacade.class));
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