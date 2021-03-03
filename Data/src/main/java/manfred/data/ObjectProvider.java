package manfred.data;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.persistence.ObjectReader;

public abstract class ObjectProvider<Source, Dto, Object> {

    private final ObjectReader<Source, Dto> objectReader;
    private final ObjectConverter<Dto, Object> objectConverter;

    protected ObjectProvider(ObjectReader<Source, Dto> objectReader, ObjectConverter<Dto, Object> objectConverter) {
        this.objectReader = objectReader;
        this.objectConverter = objectConverter;
    }

    public Object provide(String name) throws InvalidInputException {
        return objectConverter.convert(objectReader.load(name));
    }

    public Object provide(Source source) throws InvalidInputException {
        return objectConverter.convert(objectReader.load(source));
    }
}
