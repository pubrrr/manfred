package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.OrRule;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrRuleTest {

    @Test
    void firstApplies() {
        TileConversionAction<MapTile> action = NotAccessible::new;

        TileConversionRule<MapTile> first = mockTileConversionRule();
        when(first.applicableTo(any(), any())).thenReturn(Optional.of(action));
        TileConversionRule<MapTile> second = mockTileConversionRule();
        when(second.applicableTo(any(), any())).thenReturn(Optional.empty());

        OrRule<MapTile> underTest = new OrRule<>(first, second);

        Optional<TileConversionAction<MapTile>> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void secondApplies() {
        TileConversionAction<MapTile> action = NotAccessible::new;

        TileConversionRule<MapTile> first = mockTileConversionRule();
        when(first.applicableTo(any(), any())).thenReturn(Optional.empty());
        TileConversionRule<MapTile> second = mockTileConversionRule();
        when(second.applicableTo(any(), any())).thenReturn(Optional.of(action));

        OrRule<MapTile> underTest = new OrRule<>(first, second);

        Optional<TileConversionAction<MapTile>> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void noneApplies() {
        TileConversionRule<MapTile> first = mockTileConversionRule();
        when(first.applicableTo(any(), any())).thenReturn(Optional.empty());
        TileConversionRule<MapTile> second = mockTileConversionRule();
        when(second.applicableTo(any(), any())).thenReturn(Optional.empty());

        OrRule<MapTile> underTest = new OrRule<>(first, second);

        Optional<TileConversionAction<MapTile>> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(false));
    }

    private MapPrototype validatedMapDto() {
        return mock(MapPrototype.class);
    }

    @SuppressWarnings("unchecked")
    private TileConversionRule<MapTile> mockTileConversionRule() {
        return mock(TileConversionRule.class);
    }
}