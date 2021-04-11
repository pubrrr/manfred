package manfred.manfreditor.map.model.objectfactory;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.tile.MapTileStructurePrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.MapObjectRepository;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcreteMapObjectFactoryTest {

    private ConcreteMapObjectFactory underTest;
    private MapObjectRepository mapObjectRepositoryMock;

    @BeforeEach
    void setUp() {
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        underTest = new ConcreteMapObjectFactory(mapObjectRepositoryMock);
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

        ConcreteMapObject mapObjectMock = mock(ConcreteMapObject.class);
        when(mapObjectRepositoryMock.getOrCreate(any())).thenReturn(mapObjectMock);

        Optional<TileConversionAction<MapObject>> result = underTest.applicableTo(input, mock(MapPrototype.Coordinate.class));

        assertTrue(result.isPresent());
        MapObject resultingObject = result.get().create();
        assertThat(resultingObject, is(mapObjectMock));
    }

    private TilePrototype tilePrototypeWithObject() {
        ValidatedMapTileDto tileDto = new ValidatedMapTileDto("tileName", mockTileStructure(), someImage(), someImageData());
        TilePrototype tilePrototypeMock = mock(TilePrototype.class);
        when(tilePrototypeMock.getTileObject()).thenReturn(Optional.of(tileDto));
        return tilePrototypeMock;
    }

    private MapTileStructurePrototype mockTileStructure() {
        return mock(MapTileStructurePrototype.class);
    }

    private BufferedImage someImage() {
        return new BufferedImage(1, 2, 1);
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(1, 1, 1));
    }
}