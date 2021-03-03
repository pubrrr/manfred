package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.game.interact.Portal;
import manfred.game.map.MapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PortalTileFactoryTest {

    private PortalTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new PortalTileFactory();
    }

    @Test
    void noPortalsGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getPortal(any())).thenReturn(Optional.empty());

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void portalGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getPortal(any())).thenReturn(Optional.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0))));

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Portal.class));
    }
}