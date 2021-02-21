package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class MapObjectRepositoryTest {

    private MapObjectRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapObjectRepository();
    }

    @Test
    void getOrCreate() {
        ValidatedMapTileDto validatedMapTileDto = new ValidatedMapTileDto(
            "tileName",
            mockMapMatrix(),
            new BufferedImage(1, 1, 2)
        );

        ConcreteMapObject result = underTest.getOrCreate(validatedMapTileDto);

        assertThat(result.getName(), is("tileName"));
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<String> mockMapMatrix() {
        return mock(MapMatrix.class);
    }
}