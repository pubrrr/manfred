package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.tile.AccessiblePrototype;
import manfred.data.infrastructure.map.tile.NonAccessiblePrototype;
import manfred.game.map.MapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NonAccessibleTileFactoryTest {

    private NonAccessibleTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new NonAccessibleTileFactory();
    }

    @Test
    void create() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getFromMap(any())).thenReturn(new NonAccessiblePrototype());

        Optional<TileConversionAction<MapTile>> tileConversionAction = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(true));

        MapTile createdTile = tileConversionAction.get().create();
        assertThat(createdTile.isAccessible(), is(false));
    }

    @Test
    void doesNotCreateForNonAccessibleTile() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getFromMap(any())).thenReturn(new AccessiblePrototype());

        Optional<TileConversionAction<MapTile>> tileConversionAction = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(false));
    }
}