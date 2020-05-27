package manfred.game.interact.gelaber;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GelaberTest {
    Gelaber underTest;
    AbstractGelaberText gelaberTextMock;

    @BeforeEach
    void setup() {
        gelaberTextMock = mock(AbstractGelaberText.class);

        underTest = new Gelaber(new AbstractGelaberText[]{gelaberTextMock});
    }

    @Test
    void givenNextGelaber_thenReturnsNull() {
        setupGelaberTextMock(true, mock(AbstractGelaberText.class));

        Consumer<KeyControls> result = underTest.next();
        assertNull(result);
    }

    @Test
    void givenContinueTalking_thenReturnsNull() {
        setupGelaberTextMock(true, null);

        Consumer<KeyControls> result = underTest.next();
        assertNull(result);
    }

    @Test
    void givenNotContinueTalking_thenReturnsCallbackThatSetsControlsToManfred() {
        setupGelaberTextMock(false, null);

        Consumer<KeyControls> result = underTest.next();

        assertNotNull(result);

        KeyControls keyControlsMock = mock(KeyControls.class);
        when(keyControlsMock.getGamePanel()).thenReturn(mock(GamePanel.class));
        result.accept(keyControlsMock);
        verify(keyControlsMock, atLeastOnce()).controlManfred();
    }

    private void setupGelaberTextMock(boolean continueTalking, @Nullable AbstractGelaberText nextGelaber) {
        GelaberNextResponse gelaberNextResponseMock = mock(GelaberNextResponse.class);
        when(gelaberNextResponseMock.continueTalking()).thenReturn(continueTalking);
        when(gelaberNextResponseMock.getNextGelaber()).thenReturn(nextGelaber);

        when(gelaberTextMock.next()).thenReturn(gelaberNextResponseMock);
    }
}