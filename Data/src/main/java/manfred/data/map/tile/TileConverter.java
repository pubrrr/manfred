package manfred.data.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class TileConverter {

    private final MapTileReader mapTileReader;

    private final List<String> validationMessages = new LinkedList<>();

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
