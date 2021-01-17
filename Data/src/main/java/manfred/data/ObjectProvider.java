package manfred.data;

import java.util.LinkedList;
import java.util.List;

public abstract class ObjectProvider<Dto, Object> {

    private final ObjectReader<Dto> objectReader;
    private final ObjectConverter<Dto, Object> objectConverter;

    private final List<String> errorMessages = new LinkedList<>();

    protected ObjectProvider(ObjectReader<Dto> objectReader, ObjectConverter<Dto, Object> objectConverter) {
        this.objectReader = objectReader;
        this.objectConverter = objectConverter;
    }

    public Object provide(String name) throws InvalidInputException {
        return objectConverter.convert(objectReader.load(name));
    }

    public Object provideCollectingErrors(String name) {
        try {
            return this.provide(name);
        } catch (InvalidInputException e) {
            this.errorMessages.add(e.getMessage());
            return null;
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}