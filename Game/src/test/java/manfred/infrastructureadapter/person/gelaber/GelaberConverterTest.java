package manfred.infrastructureadapter.person.gelaber;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.interact.person.gelaber.GelaberFacadeBuilder;
import manfred.game.interact.person.gelaber.LineSplitter;
import manfred.game.interact.person.gelaber.SimpleTextLine;
import manfred.game.interact.person.textLineFactory.SimpleTextLineFactory;
import manfred.game.interact.person.textLineFactory.TextLineFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static helpers.GelaberHelper.setupGelaber;
import static org.hamcrest.MatcherAssert.assertThat;
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
    void happyPath() throws InvalidInputException {
        when(simpleTextLineFactoryMock.applicableTo(any())).thenReturn(Optional.of(simpleTextLineFactoryMock));
        when(simpleTextLineFactoryMock.create(any(), any())).thenReturn(mock(SimpleTextLine.class));

        GelaberPrototype input = setupGelaber(
            Map.of(
                "first", List.of("second"),
                "second", List.of("first", "second")
            ),
            "first"
        );

        GelaberFacade result = underTest.convert(input);

        assertThat(result, instanceOf(GelaberFacade.class));
    }
}