package manfred.manfreditor.map.object.factory;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.map.object.ConcreteMapObject;
import manfred.manfreditor.map.object.MapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcreteMapObjectFactoryTest {

    private ConcreteMapObjectFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new ConcreteMapObjectFactory();
    }

    @Test
    void doesNotApply() {
        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.empty());

        MapPrototype input = mock(MapPrototype.class);
        when(input.getFromMap(any())).thenReturn(tilePrototypeMock);

        Optional<TileConversionAction<MapObject>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void applies() {
        TilePrototype tilePrototypeMock = tilePrototypeWithObject();
        MapPrototype input = mock(MapPrototype.class);
        when(input.getFromMap(any())).thenReturn(tilePrototypeMock);

        Optional<TileConversionAction<MapObject>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapObject resultingObject = result.get().create();
        assertThat(resultingObject, instanceOf(ConcreteMapObject.class));
    }

    private TilePrototype tilePrototypeWithObject() {
        ValidatedMapTileDto tileDto = new ValidatedMapTileDto("tileName", mockTileStructure(), someImage());
        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.of(tileDto));
        return tilePrototypeMock;
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<String> mockTileStructure() {
        return mock(MapMatrix.class);
    }

    private BufferedImage someImage() {
        return new BufferedImage(1, 2, 1);
    }
}