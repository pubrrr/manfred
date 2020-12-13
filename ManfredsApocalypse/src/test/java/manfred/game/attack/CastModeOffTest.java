package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CastModeOffTest {
    private CastModeOff underTest;

    @BeforeEach
    void setUp() {
        underTest = new CastModeOff(mock(CastModeOn.class));
    }

    @Test
    void turnsOnCastMode() {
        CastMode result = underTest.cast(mock(Sprite.class), Direction.RIGHT);

        assertTrue(result instanceof CastModeOn);
    }
}