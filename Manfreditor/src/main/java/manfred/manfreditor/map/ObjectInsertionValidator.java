package manfred.manfreditor.map;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class ObjectInsertionValidator {

    public Result mayObjectBeInserted(ConcreteMapObject mapObject, TileCoordinate tileCoordinate, java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        java.util.Map<TileCoordinate, TilePrototype> objectStructure = mapObject.getStructureAt(tileCoordinate);

        List<String> validationMessages = objectStructure.entrySet().stream()
            .filter(tilePrototypeByCoordinate -> !tilePrototypeByCoordinate.getValue().isAccessible())
            .filter(tilePrototypeByCoordinate -> !mergedAccessibility.get(tilePrototypeByCoordinate.getKey()).isAccessible())
            .map(java.util.Map.Entry::getKey)
            .map(toErrorMessage(mergedAccessibility))
            .collect(toList());

        return validationMessages.isEmpty()
            ? Result.success()
            : Result.failedWithMessages(validationMessages);
    }

    private Function<TileCoordinate, String> toErrorMessage(java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        return tileCoordinateOnStructure -> "Tile (" + tileCoordinateOnStructure.getX() + "," + tileCoordinateOnStructure.getY() + ") " +
            "is not accessible, blocked by " + getBlockingObject(mergedAccessibility, tileCoordinateOnStructure);
    }

    private String getBlockingObject(java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility, TileCoordinate tileCoordinateOnStructure) {
        return mergedAccessibility.get(tileCoordinateOnStructure).getSource()
            .map(source -> "object " + source.getTileName() + " at (" + source.getTileCoordinate().getX() + "," + source.getTileCoordinate().getY() + ")")
            .orElse("no object");
    }

    public interface Result {

        boolean wasSuccessful();

        List<String> getValidationMessages();

        static Result success() {
            return new Success();
        }

        static Result failedWithMessages(List<String> validationMessages) {
            return new Failure(validationMessages);
        }
    }

    private static class Success implements Result {

        @Override
        public boolean wasSuccessful() {
            return true;
        }

        @Override
        public List<String> getValidationMessages() {
            return List.of();
        }
    }

    @AllArgsConstructor
    private static class Failure implements Result {

        private final List<String> validationMessages;

        @Override
        public boolean wasSuccessful() {
            return false;
        }

        @Override
        public List<String> getValidationMessages() {
            return this.validationMessages;
        }
    }
}
