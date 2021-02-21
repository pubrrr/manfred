package manfred.infrastructureadapter.map.tile;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.shared.PositiveInt;
import manfred.game.interact.person.Person;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.MapTile;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;
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

class PersonTileFactoryTest {

    private PersonTileFactory underTest;
    private GelaberConverter gelaberConverterMock;

    @BeforeEach
    void setUp() {
        gelaberConverterMock = mock(GelaberConverter.class);
        underTest = new PersonTileFactory(new TestGameConfig(), gelaberConverterMock);
    }

    @Test
    void noPersonsGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getPerson(any())).thenReturn(Optional.empty());

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void personGiven() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getPerson(any())).thenReturn(Optional.of(new PersonPrototype("name", mock(GelaberPrototype.class), null, PositiveInt.of(0), PositiveInt.of(0))));

        when(gelaberConverterMock.convert(any())).thenReturn(mock(GelaberFacade.class));

        Optional<TileConversionAction> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Person.class));
    }
}