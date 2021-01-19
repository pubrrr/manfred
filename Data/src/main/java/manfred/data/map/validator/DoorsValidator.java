package manfred.data.map.validator;

import manfred.data.map.MapHelper;
import manfred.data.map.RawMapDto;
import manfred.data.map.TransporterDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DoorsValidator extends TransporterDtoValidator implements Validator {

    private final static Predicate<Map.Entry<String, Boolean>> ACCESSIBLE_TILES = Map.Entry::getValue;

    public DoorsValidator(MapHelper mapHelper) {
        super(mapHelper);
    }

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<TransporterDto> doors = rawMapDto.getDoors();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(doors));
        validationMessages.addAll(validateAccessibility(mapMatrix, doors, ACCESSIBLE_TILES));

        return validationMessages;
    }

    @Override
    protected String transporterType() {
        return "door";
    }
}
