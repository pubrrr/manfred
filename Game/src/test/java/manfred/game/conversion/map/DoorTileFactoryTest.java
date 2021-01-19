package manfred.game.conversion.map;

import manfred.data.map.TransporterDto;
import manfred.data.map.ValidatedMapDto;
import manfred.data.map.matrix.MapMatrix;
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
        ValidatedMapDto input = new ValidatedMapDto(
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
        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(),
            List.of(new TransporterDto("target", 0, 0, 99, 99)),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void doorGiven() {
        int positionX = 5;
        int positionY = 10;

        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(),
            List.of(new TransporterDto("target", 0, 0, positionX, positionY)),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, positionX, positionY);

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Door.class));
    }
}