package manfred.manfreditor.map;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ObjectInsertionValidator {

    public Result mayObjectBeInserted(ConcreteMapObject mapObject, TileCoordinate tileCoordinate, java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        MapPrototype objectStructure = mapObject.getStructure();

        List<String> validationMessages = objectStructure.getCoordinateSet().stream()
            .filter(coordinate -> !objectStructure.getFromMap(coordinate).isAccessible())
            .map(tileCoordinate::translateBy)
            .filter(tileCoordinateOnStructure -> !mergedAccessibility.get(tileCoordinateOnStructure).isAccessible())
            .map(tileCoordinateOnStructure -> "Tile (" + tileCoordinateOnStructure.getX() + "," + tileCoordinate.getY() + ") is not accessible")
            .collect(toList());

        return validationMessages.isEmpty()
            ? Result.success()
            : Result.failedWithMessages(validationMessages);
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
