package manfred.data;

public interface ObjectConverter<Dto, Object> {

    Object convert(Dto dto) throws InvalidInputException;
}
