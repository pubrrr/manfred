package manfred.game.interact.person.gelaber;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;
import manfred.game.interact.person.textLineFactory.SimpleTextLineFactory;
import manfred.game.interact.person.textLineFactory.TextLineFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GelaberConverterTest {
    private GelaberConverter underTest;
    private SimpleTextLineFactory simpleTextLineFactoryMock;

    @BeforeEach
    void init() {
        simpleTextLineFactoryMock = mock(SimpleTextLineFactory.class);

        underTest = new GelaberConverter(
            new GelaberFacadeBuilder(new TextLineFactory(simpleTextLineFactoryMock)),
            new LineSplitter(1)
        );
    }

    @Test
    void emptyGelaber() {
        GelaberDto input = setupGelaber(Map.of(), "unknown");

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.convert(input));
        assertThat(exception.getMessage(), containsString("Unknown initial gelaber reference unknown not found"));
    }

    @Test
    void referenceNotFound() {
        GelaberDto input = setupGelaber(Map.of("key", List.of("unknown")), "key");

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.convert(input));
        assertThat(exception.getMessage(), containsString("Unknown gelaber reference"));
    }

    @Test
    void happyPath() throws InvalidInputException {
        when(simpleTextLineFactoryMock.applicableTo(any())).thenReturn(Optional.of(simpleTextLineFactoryMock));
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

    // TODO modify (delete?) this
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