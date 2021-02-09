package manfred.infrastructureadapter.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.shared.PositiveInt;
import manfred.game.interact.Door;
import manfred.game.map.MapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DoorTileFactoryTest {

    private DoorTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new DoorTileFactory();
    }

    @Test
    void noDoorsGiven() {
        MapPrototype input = new MapPrototype(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void doorAtWrongPositionGiven() {
        MapPrototype input = new MapPrototype(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(99), PositiveInt.of(99))),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void doorGiven() {
        int positionX = 5;
        int positionY = 10;

        MapPrototype input = new MapPrototype(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(positionX), PositiveInt.of(positionY))),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, positionX, positionY);

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Door.class));
    }
}