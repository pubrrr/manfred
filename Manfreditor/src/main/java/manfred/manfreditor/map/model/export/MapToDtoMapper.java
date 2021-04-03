package manfred.manfreditor.map.model.export;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import manfred.data.persistence.dto.RawMapDto;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.Source;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class MapToDtoMapper {

    public RawMapDto map(FlattenedMap flattenedMap) {
        RawMapDto rawMapDto = new RawMapDto();
        rawMapDto.setName(flattenedMap.getName());
        rawMapDto.setMap(collapseMap(flattenedMap));
        return rawMapDto;
    }

    private List<String> collapseMap(FlattenedMap flattenedMap) {
        Map<Integer, ? extends Map<TileCoordinate, String>> tileRepresentationsGroupedByYCoordinate = flattenedMap
            .getAccessibility()
            .mapValues(AccessibilityIndicator::getSource)
            .map(toStringRepresentation())
            .groupBy(tileRepresentationByCoordinate -> tileRepresentationByCoordinate._1.getY().value());

        return tileRepresentationsGroupedByYCoordinate
            .toSortedMap((first, second) -> Integer.compare(second, first), Function.identity())
            .mapValues(tilesByYCoordinate -> tilesByYCoordinate.toSortedMap(sortByXCoordinate(), Function.identity()).values())
            .values()
            .map(tilesByYCoordinate -> String.join(",", tilesByYCoordinate))
            .toJavaList();
    }

    private Comparator<TileCoordinate> sortByXCoordinate() {
        return Comparator.comparingInt(o -> o.getX().value());
    }

    private BiFunction<TileCoordinate, Optional<Source>, Tuple2<TileCoordinate, String>> toStringRepresentation() {
        return (tileCoordinate, optionalSource) -> Tuple.of(
            tileCoordinate,
            optionalSource.map(source -> getPrefix(tileCoordinate, source) + source.getTileName()).orElse("1")
        );
    }

    private String getPrefix(TileCoordinate tileCoordinate, Source source) {
        return tileCoordinate.equals(source.getTileCoordinate()) ? "" : "_";
    }
}
