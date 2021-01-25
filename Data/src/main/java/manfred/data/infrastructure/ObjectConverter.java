package manfred.data.infrastructure;

import manfred.data.InvalidInputException;

public interface ObjectConverter<Dto, Object> {

    Object convert(Dto dto) throws InvalidInputException;
}
