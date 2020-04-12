package manfred.game.characters;

import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManfredTest {
    private Manfred manfred;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        MapWrapper mapWrapperMock = mock(MapWrapper.class);

        manfred = new Manfred(0, 0, colliderMock, mapWrapperMock);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        manfred.move();

        assertEquals(initialX, manfred.getX());
        assertEquals(initialY, manfred.getY());
    }
}