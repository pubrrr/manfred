package manfred.data.infrastructure.map.validator;

import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
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
        RawMapDto input = new RawMapDto("name", List.of(), List.of(), List.of(), List.of(), List.of());

        List<String> result = underTest.validate(input, mock(MapMatrix.class));

        assertThat(result, empty());
    }

    @Test
    void objectsAtDifferentPositions() {
        RawMapDto input = new RawMapDto(
            "name",
            List.of(),
            List.of(new MapPersonDto("name", 0, 0)),
            List.of(new TransporterDto("target", 0, 0, 10, 10)),
            List.of(new TransporterDto("target", 0, 0, 20, 20)),
            List.of(new MapEnemyDto("name", 30, 30))
        );

        List<String> result = underTest.validate(input, mock(MapMatrix.class));

        assertThat(result, empty());
    }

    @Test
    void objectsAtSamePositions() {
        int position1_x = 0;
        int position1_y = 5;
        int position2_x = 10;
        int position2_y = 15;

        RawMapDto input = new RawMapDto(
            "name",
            List.of(),
            List.of(new MapPersonDto("name", position1_x, position1_y)),
            List.of(new TransporterDto("target", 0, 0, position1_x, position1_y)),
            List.of(new TransporterDto("target", 0, 0, position2_x, position2_y)),
            List.of(new MapEnemyDto("name", position2_x, position2_y))
        );

        List<String> result = underTest.validate(input, mock(MapMatrix.class));

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(
            "The following map objects are at the same position:\n[TransporterDto(target=target, targetSpawnX=0, targetSpawnY=0, positionX=10, positionY=15), MapEnemyDto(name=name, positionX=10, positionY=15)]",
            "The following map objects are at the same position:\n[TransporterDto(target=target, targetSpawnX=0, targetSpawnY=0, positionX=0, positionY=5), MapPersonDto(name=name, positionX=0, positionY=5)]"
        ));
    }
}