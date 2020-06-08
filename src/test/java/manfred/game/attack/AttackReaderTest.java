package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackReaderTest {
    private AttackReader underTest;

    @BeforeEach
    void init() throws Exception {
        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mock(MapCollider.class));

        underTest = new AttackReader(mapColliderProviderMock);
    }

    @Test
    void convert() throws InvalidInputException {
        String input = "{speed: 1, sizeX: 0, sizeY: 0}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.right);
        attack.move();
        assertEquals(1, attack.getX());
        assertEquals(0, attack.getY());
    }
}