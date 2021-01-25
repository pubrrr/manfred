package manfred.data;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.persistence.ObjectReader;

public abstract class ObjectProvider<Dto, Object> {

    private final ObjectReader<Dto> objectReader;
    private final ObjectConverter<Dto, Object> objectConverter;

    protected ObjectProvider(ObjectReader<Dto> objectReader, ObjectConverter<Dto, Object> objectConverter) {
        this.objectReader = objectReader;
        this.objectConverter = objectConverter;
    }

    public Object provide(String name) throws InvalidInputException {
        return objectConverter.convert(objectReader.load(name));
    }
}
