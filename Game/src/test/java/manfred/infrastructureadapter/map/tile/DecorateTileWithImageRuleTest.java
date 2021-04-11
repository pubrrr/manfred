package manfred.infrastructureadapter.map.tile;

import helpers.TestGameConfig;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.infrastructure.map.tile.MapTileStructurePrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.shared.PositiveInt;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.MapTile;
import manfred.game.map.MapTileWithSprite;
import manfred.game.map.NotAccessible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DecorateTileWithImageRuleTest {

    private DecorateTileWithImageRule underTest;

    private TileConversionRule<MapTile> wrappedRuleMock;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        wrappedRuleMock = mock(TileConversionRule.class);
        underTest = DecorateTileWithImageRule.builder(new TestGameConfig()).and(wrappedRuleMock);
    }

    @Test
    void wrappedRuleNotApplicable_noObjectGiven() {
        when(wrappedRuleMock.applicableTo(any(), any())).thenReturn(Optional.empty());

        MapPrototype input = prepareMapTileWithoutObject();

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedRuleNotApplicable_objectGiven() {
        MapPrototype input = prepareMapTileWithObject();

        when(wrappedRuleMock.applicableTo(any(), any())).thenReturn(Optional.empty());

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedruleApplicable_noObjectOnTileGiven() {
        when(wrappedRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = prepareMapTileWithoutObject();

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedRuleIsApplicable_objectGiven() {
        when(wrappedRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = prepareMapTileWithObject();

        Optional<TileConversionAction<MapTile>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapTile resultingTile = result.get().create();
        assertThat(resultingTile, instanceOf(MapTileWithSprite.class));
        assertTilePaintsImage(resultingTile);
    }

    private void assertTilePaintsImage(MapTile resultingTile) {
        GraphicsAdapter graphicsMock = mock(GraphicsAdapter.class);
        resultingTile.paint(graphicsMock, new PanelCoordinate(0, 0));
        verify(graphicsMock).drawSprite(any());
    }

    private MapPrototype prepareMapTileWithObject() {
        MapPrototype.Coordinate originCoordinate = mock(MapPrototype.Coordinate.class);
        when(originCoordinate.getY()).thenReturn(PositiveInt.of(1));

        MapTileStructurePrototype structureMock = mock(MapTileStructurePrototype.class);
        when(structureMock.getSizeX()).thenReturn(PositiveInt.ofNonZero(1));
        when(structureMock.getBottomLeftCoordinate()).thenReturn(originCoordinate);
        ValidatedMapTileDto mapTileDto = new ValidatedMapTileDto("tileName", structureMock, new BufferedImage(1, 2, 1), null);

        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.of(mapTileDto));

        MapPrototype mapPrototype = mock(MapPrototype.class);
        when(mapPrototype.getFromMap(any())).thenReturn(tilePrototypeMock);

        return mapPrototype;
    }

    private MapPrototype prepareMapTileWithoutObject() {
        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.empty());

        MapPrototype mapPrototype = mock(MapPrototype.class);
        when(mapPrototype.getFromMap(any())).thenReturn(tilePrototypeMock);

        return mapPrototype;
    }
}