package manfred.data.map.matrix;

import lombok.Getter;
import manfred.data.InvalidInputException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        private final List<List<String>> rawMapMatrix;

        private final List<String> validationMessages = new LinkedList<>();

        public Builder(List<List<String>> rawMapMatrix) {
            this.rawMapMatrix = rawMapMatrix;
        }

        public MapMatrix validateAndBuild() throws InvalidInputException {
            int width = getMapWidth();
            int height = rawMapMatrix.size();

            validationMessages.addAll(validateMapIsSquare(width));

            List<List<String>> validatedMatrix = transposeMap(width, height);

            if (!validationMessages.isEmpty()) {
                throw new InvalidInputException("Validation of map failed:\n" + String.join(",\n", this.validationMessages));
            }
            return new MapMatrix(validatedMatrix);
        }

        private List<List<String>> transposeMap(int width, int height) {
            List<List<String>> validatedMatrix = new ArrayList<>(width);
            for (int x = 0; x < width; x++) {
                List<String> column = new ArrayList<>(height);
                for (List<String> rawRow : rawMapMatrix) {
                    column.add(rawRow.get(x));
                }
                validatedMatrix.add(column);
            }
            return validatedMatrix;
        }

        private int getMapWidth() {
            return rawMapMatrix.stream().min(Comparator.comparingInt(List::size)).orElseGet(List::of).size();
        }

        private List<String> validateMapIsSquare(int width) {
            return rawMapMatrix.stream()
                .filter(strings -> strings.size() > width)
                .map(row -> "Map row " + rawMapMatrix.indexOf(row) + " is too long, must not be longer than " + width + ", was: " + String.join(",", row))
                .collect(toList());
        }
    }
}
