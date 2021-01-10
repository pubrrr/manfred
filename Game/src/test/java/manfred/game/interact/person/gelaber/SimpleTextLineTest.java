package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.GelaberEdge;
import manfred.game.interact.person.gelaber.GelaberResponseWrapper;
import manfred.game.interact.person.gelaber.SimpleTextLine;
import manfred.game.interact.person.gelaber.TextLine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class SimpleTextLineTest {

    @Test
    void next() {
        GelaberEdge edgeMock = mock(GelaberEdge.class);
        SimpleTextLine underTest = new SimpleTextLine(List.of("test"), mock(GameConfig.class), edgeMock);

        TextLine textLineMock = mock(TextLine.class);
        GelaberResponseWrapper result = underTest.next(identifier -> textLineMock);

        assertSame(textLineMock, result.getNextTextLine());
    }
}