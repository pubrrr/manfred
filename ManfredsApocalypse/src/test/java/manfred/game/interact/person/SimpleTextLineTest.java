package manfred.game.interact.person;

import manfred.game.GameConfig;
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