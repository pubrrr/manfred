package manfred.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManfredTest {
    private Manfred manfred;

    @BeforeEach
    void init() {
        manfred = new Manfred(0, 0);
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