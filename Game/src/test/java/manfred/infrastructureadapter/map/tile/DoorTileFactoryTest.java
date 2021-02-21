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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DoorTileFactoryTest {

    private DoorTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new DoorTileFactory();
    }

    @Test
    void noDoorsGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getDoor(any())).thenReturn(Optional.empty());

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void doorGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getDoor(any())).thenReturn(Optional.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(99), PositiveInt.of(99))));

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Door.class));
    }
}