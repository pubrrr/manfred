package manfred.game.conversion.map;

import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.game.interact.Portal;
import manfred.game.map.MapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PortalTileFactoryTest {

    private PortalTileFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new PortalTileFactory();
    }

    @Test
    void noPortalsGiven() {
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
    void portalAtWrongPositionGiven() {
        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(new TransporterDto("target", 0, 0, 99, 99)),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void portalGiven() {
        int positionX = 5;
        int positionY = 10;

        ValidatedMapDto input = new ValidatedMapDto(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(new TransporterDto("target", 0, 0, positionX, positionY)),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, positionX, positionY);

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Portal.class));
    }
}