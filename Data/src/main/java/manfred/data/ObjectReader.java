package manfred.data;

public interface ObjectReader<Dto> {
    Dto load(String name) throws InvalidInputException;
}
