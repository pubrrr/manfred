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
        when(first.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(action));
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), 0, 0);

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void secondApplies() {
        TileConversionAction action = NotAccessible::new;

        TileConversionRule first = mock(TileConversionRule.class);
        when(first.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(action));

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), 0, 0);

        assertThat(tileConversionAction.isPresent(), is(true));
        assertThat(tileConversionAction.get(), is(action));
    }

    @Test
    void noneApplies() {
        TileConversionRule first = mock(TileConversionRule.class);
        when(first.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());
        TileConversionRule second = mock(TileConversionRule.class);
        when(second.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        OrRule underTest = new OrRule(first, second);

        Optional<TileConversionAction> tileConversionAction = underTest.applicableTo(validatedMapDto(), 0, 0);

        assertThat(tileConversionAction.isPresent(), is(false));
    }

    private MapPrototype validatedMapDto() {
        return new MapPrototype(null, null, null, null, null, null);
    }
}