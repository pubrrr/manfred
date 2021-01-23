package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class TileConverter {

    private final MapTileReader mapTileReader;

    private final List<String> validationMessages = new LinkedList<>();

    public TileConverter(MapTileReader mapTileReader) {
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

        return isAccessible(validatedMapTileDto.getStructure().bottomLeft())
            ? TilePrototype.withObject(validatedMapTileDto).andAccessible()
            : TilePrototype.withObject(validatedMapTileDto).andNotAccessible();
    }

    private boolean isNotAccessible(String tileValue) {
        return tileValue.startsWith("_") || tileValue.equals("0");
    }

    private boolean isAccessible(String tileValue) {
        return tileValue.equals("1");
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
