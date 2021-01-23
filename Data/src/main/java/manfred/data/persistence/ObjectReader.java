package manfred.data.persistence;

import manfred.data.InvalidInputException;

public interface ObjectReader<Dto> {
    Dto load(String name) throws InvalidInputException;
}
