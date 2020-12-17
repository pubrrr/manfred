package manfred.game.interact.gelaber;

import helpers.TestGameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GelaberTextTest {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    GelaberText underTest;

    @Test
    void givenOnlyOneLine_thenEnterTriggersControllingManfred() {
        underTest = new GelaberText(new String[]{"line1"}, (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES));

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response = underTest.next();


        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{mock(GelaberText.class)});
        GelaberController gelaberControllerMock = mock(GelaberController.class);
        response.apply(gelaber).apply(gelaberControllerMock);
        verify(gelaberControllerMock).previous();
    }

    @Test
    void givenEnoughLines_thenEnterDoesNotTriggerControllingManfred() {
        underTest = new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5", "line6", "line7", "line8"}, (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES));

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response = underTest.next();

        GelaberController gelaberControllerMock = mock(GelaberController.class);
        ControllerInterface newControllerState = response.apply(mock(Gelaber.class)).apply(gelaberControllerMock);
        assertSame(gelaberControllerMock, newControllerState);
    }
}