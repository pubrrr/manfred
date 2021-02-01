package manfred.infrastructureadapter.map.tile;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.game.map.MapTile;
import manfred.game.map.MapTileWithImageDecorator;
import manfred.game.map.NotAccessible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DecorateTileWithImageRuleTest {

    private DecorateTileWithImageRule underTest;

    private TileConversionRule wrappedRuleMock;

    @BeforeEach
    void setUp() throws InvalidInputException {
        wrappedRuleMock = mock(TileConversionRule.class);
        underTest = DecorateTileWithImageRule.build(new TestGameConfig()).and(wrappedRuleMock);
    }

    @Test
    void wrappedRuleNotApplicable_noObjectGiven() {
        when(wrappedRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        MapPrototype input = prepareMapTileWithoutObject();

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedRuleNotApplicable_objectGiven() {
        MapPrototype input = prepareMapTileWithObject();

        when(wrappedRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedruleApplicable_noObjectOnTileGiven() {
        when(wrappedRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = prepareMapTileWithoutObject();

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void wrappedRuleIsApplicable_objectGiven() {
        when(wrappedRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = prepareMapTileWithObject();

        Optional<TileConversionAction> result = underTest.applicableTo(input, 0, 0);

        assertTrue(result.isPresent());
        MapTile resultingTile = result.get().create();
        assertThat(resultingTile, instanceOf(MapTileWithImageDecorator.class));
        assertTilePaintsImage(resultingTile);
    }

    private void assertTilePaintsImage(MapTile resultingTile) {
        Graphics graphicsMock = mock(Graphics.class);
        resultingTile.paint(graphicsMock, new Point(0, 0), 0, 0);
        verify(graphicsMock).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), isNull());
    }

    private MapPrototype prepareMapTileWithObject() {

        ValidatedMapTileDto mapTileDto = new ValidatedMapTileDto("tileName", mock(MapMatrix.class), new BufferedImage(1, 2, 1));

        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.of(mapTileDto));

        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(tilePrototypeMock);

        return validatedMapDto(mapMatrixMock);
    }

    private MapPrototype prepareMapTileWithoutObject() {
        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.empty());

        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(tilePrototypeMock);

        return validatedMapDto(mapMatrixMock);
    }

    private MapPrototype validatedMapDto(MapMatrix<TilePrototype> mapMatrix) {
        return new MapPrototype("name", mapMatrix, List.of(), List.of(), List.of(), List.of());
    }
}