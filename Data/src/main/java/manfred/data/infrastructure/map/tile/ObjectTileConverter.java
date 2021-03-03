package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ObjectTileConverter extends TileConverter {

    private final MapTileReader mapTileReader;

    private final List<String> validationMessages = new LinkedList<>();

    public ObjectTileConverter(MapTileReader mapTileReader) {
        this.mapTileReader = mapTileReader;
    }

    public TilePrototype stringToObject(String tileValue) {
        if (isNotAccessible(tileValue)) {
            return TilePrototype.notAccessible();
        }
        if (isAccessible(tileValue)) {
            return TilePrototype.accessible();
        }

        ValidatedMapTileDto validatedMapTileDto;
        try {
            validatedMapTileDto = mapTileReader.load(tileValue);
        } catch (InvalidInputException e) {
            validationMessages.add(e.getMessage());
            return TilePrototype.notAccessible();
        }

        return validatedMapTileDto.getStructure().bottomLeft().isAccessible()
            ? TilePrototype.withObject(validatedMapTileDto).andAccessible()
            : TilePrototype.withObject(validatedMapTileDto).andNotAccessible();
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
