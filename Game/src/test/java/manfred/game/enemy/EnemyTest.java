package manfred.game.enemy;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.Velocity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyTest {
    private final static int AGGRO_RADIUS = 1600;
    private final static PositiveInt SPEED = PositiveInt.of(400);
    private final static int PIXEL_BLOCK_SIZE = 40;

    private Enemy underTest;

    private MapCollider mapColliderMock;

    @BeforeEach
    void init() throws InvalidInputException {
        mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new Enemy("name", Velocity.withSpeed(SPEED), 0, 0, PositiveInt.of(100), null, AGGRO_RADIUS, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void givenManfredOutsideAggroRadius_thenDoesNotMove() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS + 1, 0));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());

        underTest.determineSpeed(mockManfredAtCoordinates(0, AGGRO_RADIUS + 1));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @Test
    void movesHorizontallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS, 0));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX + SPEED.value(), underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @Test
    void movesVerticallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.determineSpeed(mockManfredAtCoordinates(0, AGGRO_RADIUS));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX, underTest.getX());
        assertEquals(initialY + SPEED.value(), underTest.getY());
    }

    @Test
    void movesDiagonallyTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS / 2, AGGRO_RADIUS / 2));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX + (int) ((float) SPEED.value() / Math.sqrt(2)), underTest.getX());
        assertEquals(initialY + (int) ((float) SPEED.value() / Math.sqrt(2)), underTest.getY());
    }

    @Test
    void movesAtSomeAngleTowardsManfred() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS * 3 / 4, AGGRO_RADIUS / 4));
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertEquals(initialX + (int) ((float) SPEED.value() * 3 / Math.sqrt(10)), underTest.getX());
        assertEquals(initialY + (int) ((float) SPEED.value() / Math.sqrt(10)), underTest.getY());
    }

    private Manfred mockManfredAtCoordinates(int x, int y) {
        Manfred manfredMock = mock(Manfred.class);
        when(manfredMock.getX()).thenReturn(x);
        when(manfredMock.getY()).thenReturn(y);
        return manfredMock;
    }
}