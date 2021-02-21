package manfred.infrastructureadapter.map.tile;

import helpers.TestGameConfig;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.game.map.DebugTileWrapper;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DebugWrapperFactoryTest {

    private DebugWrapperFactory underTest;
    private TileConversionRule wrappedMock;

    @BeforeEach
    void setUp() {
        wrappedMock = mock(TileConversionRule.class);
        underTest = new DebugWrapperFactory(wrappedMock, new TestGameConfig());
    }

    @Test
    void wrappedApplicable() {
        when(wrappedMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = mock(MapPrototype.class);

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());

        MapTile resultingTile = result.get().create();
        assertThat(resultingTile, is(instanceOf(DebugTileWrapper.class)));
        assertThat(resultingTile.isAccessible(), is(false));
    }

    @Test
    void wrappedNotApplicable() {
        when(wrappedMock.applicableTo(any(), any())).thenReturn(Optional.empty());

        MapPrototype input = mock(MapPrototype.class);

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }
}