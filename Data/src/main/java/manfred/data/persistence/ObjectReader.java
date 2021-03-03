package manfred.data.persistence;

import manfred.data.InvalidInputException;

public interface ObjectReader<SOURCE, DTO> {
    DTO load(String name) throws InvalidInputException;

    DTO load(SOURCE source) throws InvalidInputException;
}
