package manfred.data.infrastructure.map.validator;

import manfred.data.persistence.dto.MapObjectDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class NoTwoObjectsAtSameTileValidator implements Validator {

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<MapObjectDto> allMapObjects = new LinkedList<>();
        allMapObjects.addAll(rawMapDto.getPortals());
        allMapObjects.addAll(rawMapDto.getDoors());
        allMapObjects.addAll(rawMapDto.getPersons());
        allMapObjects.addAll(rawMapDto.getEnemies());

        return allMapObjects.stream()
            .collect(groupByPosition())
            .values().stream()
            .filter(mapObjectDtos -> mapObjectDtos.size() > 1)
            .map(this::toErrorMessage)
            .collect(toList());
    }

    private Collector<MapObjectDto, ?, Map<Point, List<MapObjectDto>>> groupByPosition() {
        return Collectors.groupingBy(mapObjectDto -> new Point(mapObjectDto.getPositionX(), mapObjectDto.getPositionY()));
    }

    private String toErrorMessage(List<MapObjectDto> mapObjectDtosAtSamePosition) {
        return "The following map objects are at the same position:\n" + String.join(",\n", mapObjectDtosAtSamePosition.toString());
    }
}
