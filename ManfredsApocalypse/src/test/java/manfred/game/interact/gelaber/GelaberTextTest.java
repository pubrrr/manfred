package manfred.game.interact.gelaber;

import helpers.TestGameConfig;
import manfred.game.controls.KeyControls;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.*;

class GelaberTextTest {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    GelaberText underTest;

    @Test
    void givenOnlyOneLine_thenEnterTriggersControllingManfred() {
        underTest = new GelaberText(new String[]{"line1"}, (new TestGameConfig()).setNumberOfTextLines(NUMBER_OF_TEXT_LINES));

        Function<Gelaber, Consumer<KeyControls>> response = underTest.next();

        Gelaber gelaberMock = mock(Gelaber.class);
        response.apply(gelaberMock);
        verify(gelaberMock).switchControlsBackToManfred();
    }

    @Test
    void givenEnoughLines_thenEnterDoesNotTriggerControllingManfred() {
        underTest = new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5", "line6", "line7", "line8"}, (new TestGameConfig()).setNumberOfTextLines(NUMBER_OF_TEXT_LINES));

        Function<Gelaber, Consumer<KeyControls>> response = underTest.next();

        KeyControls keyControlsMock = mock(KeyControls.class);
        response.apply(mock(Gelaber.class)).accept(keyControlsMock);
        verify(keyControlsMock).doNothing();
        verify(keyControlsMock, never()).controlManfred();
    }
}