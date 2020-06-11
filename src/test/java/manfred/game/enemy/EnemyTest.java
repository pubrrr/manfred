package manfred.game.enemy;

import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnemyTest {
    private final int AGGRO_RADIUS = 10;
    private final int SPEED = 4;

    private Enemy underTest;

    @BeforeEach
    void init() {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new Enemy("name", SPEED, 0, 0, 100, null, mapColliderMock, AGGRO_RADIUS);
    }

    @Test
    void givenManfredOutsideAggroRadius_thenDoesNotMove() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move(mockManfredAtCoordinates(AGGRO_RADIUS + 1, 0));
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());

        underTest.move(mockManfredAtCoordinates(0, AGGRO_RADIUS + 1));
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @Test
    void movesHorizontallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move(mockManfredAtCoordinates(AGGRO_RADIUS, 0));
        assertEquals(initialX + SPEED, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @Test
    void movesVerticallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move(mockManfredAtCoordinates(0, AGGRO_RADIUS));
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY + SPEED, underTest.getY());
    }

    @Test
    void movesDiagonallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move(mockManfredAtCoordinates(AGGRO_RADIUS / 2, AGGRO_RADIUS / 2));
        assertEquals(initialX + SPEED / 2, underTest.getX());
        assertEquals(initialY + SPEED / 2, underTest.getY());
    }

    @Test
    void movesAtSomeAngleTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move(mockManfredAtCoordinates(AGGRO_RADIUS * 3 / 4, AGGRO_RADIUS / 4));
        assertEquals(initialX + SPEED * 3 / 4, underTest.getX());
        assertEquals(initialY + SPEED / 4, underTest.getY());
    }

    private Manfred mockManfredAtCoordinates(int x, int y) {
        Manfred manfredMock = mock(Manfred.class);
        when(manfredMock.getX()).thenReturn(x);
        when(manfredMock.getY()).thenReturn(y);
        return manfredMock;
    }
}