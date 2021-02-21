package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class MapObjectRepositoryTest {

    private MapObjectRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapObjectRepository();
    }

    @Test
    void initiallyKeysAreEmpty() {
        assertThat(underTest.getKeys(), empty());
    }

    @Test
    void getOrCreateWithNewObject() {
        ValidatedMapTileDto validatedMapTileDto = validatedMapTileDto();

        ConcreteMapObject result = underTest.getOrCreate(validatedMapTileDto);

        assertThat(result.getName(), is("tileName"));

        List<MapObjectRepository.ObjectKey> keys = underTest.getKeys();
        assertThat(keys, hasSize(1));

        assertThat(underTest.get(keys.get(0)), is(result));
    }

    @Test
    void doesNotCreateNewObjectOnSecondTimeWithTileWithSameName() {
        ValidatedMapTileDto validatedMapTileDto1 = validatedMapTileDto();
        ConcreteMapObject result1 = underTest.getOrCreate(validatedMapTileDto1);
        assertThat(result1.getName(), is("tileName"));

        ValidatedMapTileDto validatedMapTileDto2 = validatedMapTileDto();
        ConcreteMapObject result2 = underTest.getOrCreate(validatedMapTileDto2);
        assertThat(result2.getName(), is("tileName"));

        List<MapObjectRepository.ObjectKey> keys = underTest.getKeys();
        assertThat(keys, hasSize(1));

        assertThat(underTest.get(keys.get(0)), is(result1));
    }

    @Test
    void getOrCreateWithObjectsWithDifferentName() {
        ValidatedMapTileDto validatedMapTileDto1 = validatedMapTileDto("name1");
        ConcreteMapObject result1 = underTest.getOrCreate(validatedMapTileDto1);
        assertThat(result1.getName(), is("name1"));

        ValidatedMapTileDto validatedMapTileDto2 = validatedMapTileDto("name2");
        ConcreteMapObject result2 = underTest.getOrCreate(validatedMapTileDto2);
        assertThat(result2.getName(), is("name2"));

        List<MapObjectRepository.ObjectKey> keys = underTest.getKeys();
        assertThat(keys, hasSize(2));

        assertThat(underTest.get(keys.get(1)), is(result1));
        assertThat(underTest.get(keys.get(0)), is(result2));
    }

    private ValidatedMapTileDto validatedMapTileDto() {
        return validatedMapTileDto("tileName");
    }

    private ValidatedMapTileDto validatedMapTileDto(String name) {
        return new ValidatedMapTileDto(
            name,
            mockMapMatrix(),
            new BufferedImage(1, 1, 2)
        );
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<TilePrototype> mockMapMatrix() {
        return mock(MapMatrix.class);
    }
}