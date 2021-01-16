package manfred.data.map.matrix;

import manfred.data.InvalidInputException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class MapMatrix<T> {

    private final List<List<T>> matrix;

    private MapMatrix(List<List<T>> matrix) {
        this.matrix = matrix;
    }

    public static <T> Builder<T> fromRawDtoData(List<List<T>> rawMapMatrix) {
        return new Builder<>(rawMapMatrix);
    }

    public <V> MapMatrix<V> convertTiles(Function<T, V> conversionFunction) {
        List<List<V>> convertedMatrix = this.matrix.stream()
            .map(column -> column.stream().map(conversionFunction).collect(toList()))
            .collect(toList());

        return new MapMatrix<>(convertedMatrix);
    }

    public T bottomLeft() {
        return this.get(0, matrix.get(0).size() - 1);
    }

    public T get(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IndexOutOfBoundsException("Indeces must not be negative, x=" + x + " y=" + y + " was given.");
        }
        if (x > matrix.size() - 1) {
            throw new IndexOutOfBoundsException("Index x=" + x + " out of bounds, maximum " + (matrix.size() - 1));
        }
        if (y > matrix.get(0).size()) {
            throw new IndexOutOfBoundsException("Index y=" + y + " out of bounds, maximum " + (matrix.get(0).size() - 1));
        }

        return matrix.get(x).get(y);
    }

    public int sizeX() {
        return this.matrix.size();
    }

    public int sizeY() {
        return this.matrix.get(0).size();
    }

    public static class Builder<T> {

        private final List<List<T>> rawMatrix;

        private final List<String> validationMessages = new LinkedList<>();

        public Builder(List<List<T>> rawMatrix) {
            this.rawMatrix = rawMatrix;
        }

        public MapMatrix<T> validateAndBuild() throws InvalidInputException {
            int width = getMapWidth(rawMatrix);
            int height = rawMatrix.size();
            if (width == 0 || height == 0) {
                throw new InvalidInputException("Map matrix must not be empty, " + rawMatrix.toString() + " was given.");
            }

            this.validationMessages.addAll(validateMapIsRectangular(rawMatrix, width));

            List<List<T>> validatedMatrix = transposeMap(rawMatrix, width, height);

            if (!this.validationMessages.isEmpty()) {
                throw new InvalidInputException("Validation of map failed:\n" + String.join(",\n", this.validationMessages));
            }
            return new MapMatrix<>(validatedMatrix);
        }

        private List<List<T>> transposeMap(List<List<T>> rawMatrix, int width, int height) {
            List<List<T>> validatedMatrix = new ArrayList<>(width);
            for (int x = 0; x < width; x++) {
                List<T> column = new ArrayList<>(height);
                for (List<T> rawRow : rawMatrix) {
                    column.add(rawRow.get(x));
                }
                validatedMatrix.add(column);
            }
            return validatedMatrix;
        }

        private int getMapWidth(List<List<T>> rawMatrix) {
            return rawMatrix.stream().min(Comparator.comparingInt(List::size)).orElseGet(List::of).size();
        }

        private List<String> validateMapIsRectangular(List<List<T>> rawMapMatrix, int width) {
            return rawMapMatrix.stream()
                .filter(strings -> strings.size() > width)
                .map(row -> "Map row " + rawMapMatrix.indexOf(row) + " is too long, must not be longer than " + width + ", was: " + String.join(",", row.toString()))
                .collect(toList());
        }
    }
}
