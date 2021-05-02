package manfred.data.infrastructure.map.validator;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;

class NoTwoObjectsAtSameTileValidatorTest {

    private NoTwoObjectsAtSameTileValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new NoTwoObjectsAtSameTileValidator();
    }

    @Test
    void emptyInput() {
        RawMapDto input = new RawMapDto("name", List.of(), List.of(), List.of(), List.of(), List.of(), null);

        List<String> result = underTest.validate(input, mockMapMatrix());

        assertThat(result, empty());
    }

    @Test
    void objectsAtDifferentPositions() {
        RawMapDto input = new RawMapDto(
            "name",
            List.of(),
            List.of(new MapPersonDto("name", PositiveInt.of(0), PositiveInt.of(0))),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(10), PositiveInt.of(10))),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(20), PositiveInt.of(20))),
            List.of(new MapEnemyDto("name", PositiveInt.of(30), PositiveInt.of(30))),
            null
        );

        List<String> result = underTest.validate(input, mockMapMatrix());

        assertThat(result, empty());
    }

    @Test
    void objectsAtSamePositions() {
        PositiveInt position1_x = PositiveInt.of(0);
        PositiveInt position1_y = PositiveInt.of(5);
        PositiveInt position2_x = PositiveInt.of(10);
        PositiveInt position2_y = PositiveInt.of(15);

        RawMapDto input = new RawMapDto(
            "name",
            List.of(),
            List.of(new MapPersonDto("name", position1_x, position1_y)),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), position1_x, position1_y)),
            List.of(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), position2_x, position2_y)),
            List.of(new MapEnemyDto("name", position2_x, position2_y)),
            null
        );

        List<String> result = underTest.validate(input, mockMapMatrix());

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(
            "The following map objects are at the same position:\n[TransporterDto(target=target, targetSpawnX=0, targetSpawnY=0, positionX=10, positionY=15), MapEnemyDto(name=name, positionX=10, positionY=15)]",
            "The following map objects are at the same position:\n[TransporterDto(target=target, targetSpawnX=0, targetSpawnY=0, positionX=0, positionY=5), MapPersonDto(name=name, positionX=0, positionY=5)]"
        ));
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<TilePrototype> mockMapMatrix() {
        return mock(MapMatrix.class);
    }
}