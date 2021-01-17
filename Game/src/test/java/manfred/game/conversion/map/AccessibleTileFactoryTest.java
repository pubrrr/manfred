package manfred.game.conversion.map;

import manfred.data.map.ValidatedMapDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.AccessiblePrototype;
import manfred.data.map.tile.NonAccessiblePrototype;
import manfred.game.map.MapTile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccessibleTileFactoryTest {

    private AccessibleTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccessibleTileFactory();
    }

    @Test
    void create() {
        MapMatrix mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(0, 0)).thenReturn(new AccessiblePrototype());
        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mapMatrixMock,
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(input, 0, 0);

        assertThat(tileConversionAction.isPresent(), is(true));

        MapTile createdTile = tileConversionAction.get().create();
        assertThat(createdTile.isAccessible(), is(true));
    }

    @Test
    void doesNotCreateForNonAccessibleTile() {
        MapMatrix mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(0, 0)).thenReturn(new NonAccessiblePrototype());
        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mapMatrixMock,
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(input, 0, 0);

        assertThat(tileConversionAction.isPresent(), is(false));
    }
}