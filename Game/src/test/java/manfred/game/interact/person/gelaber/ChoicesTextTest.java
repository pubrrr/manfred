package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;
import manfred.game.controls.GelaberController;
import manfred.game.interact.person.gelaber.ChoicesBox;
import manfred.game.interact.person.gelaber.ChoicesFacade;
import manfred.game.interact.person.gelaber.ChoicesText;
import manfred.game.interact.person.gelaber.GelaberResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ChoicesTextTest {
    private ChoicesText underTest;

    @BeforeEach
    void setUp() {
        underTest = new ChoicesText(List.of("text"), mock(GameConfig.class), mock(ChoicesFacade.class));
    }

    @Test
    void next() {
        GelaberResponseWrapper result = underTest.next(identifier -> null);

        Assertions.assertTrue(result.getNextTextLine() instanceof ChoicesBox);
        GelaberController controllerMock = mock(GelaberController.class);
        assertSame(controllerMock, result.getContinueCommand().apply(controllerMock));
    }
}