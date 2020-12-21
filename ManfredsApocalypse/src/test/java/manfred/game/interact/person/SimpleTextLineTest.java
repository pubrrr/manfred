package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class SimpleTextLineTest {

    @Test
    void next() {
        GelaberEdge edgeMock = mock(GelaberEdge.class);
        SimpleTextLine underTest = new SimpleTextLine(new String[]{"test"}, mock(GameConfig.class), edgeMock);

        GelaberEdge result = underTest.next();

        assertSame(edgeMock, result);
    }
}