package manfred.data.map.matrix;

import lombok.Getter;
import manfred.data.InvalidInputException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
public class MapMatrix {

    private final List<List<String>> matrix;

    private MapMatrix(List<List<String>> matrix) {
        this.matrix = matrix;
    }

    public static Builder fromRawData(List<List<String>> rawMapMatrix) {
        return new Builder(rawMapMatrix);
    }

    public static class Builder {
        private final int x;
        private final int y;
        private final List<List<String>> rawMapMatrix;

        private final List<String> validationMessages = new LinkedList<>();

        public Builder(List<List<String>> rawMapMatrix) {
            this.rawMapMatrix = rawMapMatrix;
            x = rawMapMatrix.stream().min(Comparator.comparingInt(List::size))
                .orElseGet(List::of)
                .size();
            y = rawMapMatrix.size();
        }

        public MapMatrix validateAndBuild() throws InvalidInputException {
            List<List<String>> matrix = new ArrayList<>(x);

            if (!validationMessages.isEmpty()) {
                throw new InvalidInputException("Validation of map failed:\n" + String.join(",\n", this.validationMessages));
            }
            return new MapMatrix(matrix);
        }
    }
}
