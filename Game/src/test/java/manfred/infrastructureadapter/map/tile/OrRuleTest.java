package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.map.NotAccessible;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrRuleTest {

    @Test
    void firstApplies() {
        TileConversionAction action = NotAccessible::new;

        TileConversionRule first = mock(TileConversionRule.class);
        when(first.applicableTo(any(), any())).thenReturn(Optional.of(action));
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), any())).thenReturn(Optional.empty());

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void secondApplies() {
        TileConversionAction action = NotAccessible::new;

        TileConversionRule first = mock(TileConversionRule.class);
        when(first.applicableTo(any(), any())).thenReturn(Optional.empty());
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), any())).thenReturn(Optional.of(action));

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void noneApplies() {
        TileConversionRule first = mock(TileConversionRule.class);
        when(first.applicableTo(any(), any())).thenReturn(Optional.empty());
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), any())).thenReturn(Optional.empty());

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), mock(MapPrototype.Coordinate.class));

        assertThat(tileConversionAction.isPresent(), is(false));
    }

    private MapPrototype validatedMapDto() {
        return mock(MapPrototype.class);
    }
}