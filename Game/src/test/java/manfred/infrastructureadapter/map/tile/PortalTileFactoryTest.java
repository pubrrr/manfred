package manfred.infrastructureadapter.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.shared.PositiveInt;
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
        MapPrototype input = new MapPrototype(
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
        MapPrototype input = new MapPrototype(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(99), PositiveInt.of(99))),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void portalGiven() {
        PositiveInt positionX = PositiveInt.of(5);
        PositiveInt positionY = PositiveInt.of(10);

        MapPrototype input = new MapPrototype(
            "name",
            mock(MapMatrix.class),
            List.of(),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), positionX, positionY)),
            List.of(),
            List.of()
        );

        Optional<TileConversionAction> result = underTest.applicableTo(input, positionX.value(), positionY.value());

        assertTrue(result.isPresent());
        MapTile createdTile = result.get().create();

        assertThat(createdTile, instanceOf(Portal.class));
    }
}