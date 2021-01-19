package manfred.data.map.validator;

import manfred.data.map.MapHelper;
import manfred.data.map.RawMapDto;
import manfred.data.map.TransporterDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PortalsValidator extends TransporterDtoValidator implements Validator {

    public PortalsValidator(MapHelper mapHelper) {
        super(mapHelper);
    }

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<TransporterDto> portals = rawMapDto.getPortals();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(portals));
        validationMessages.addAll(validateAccessibility(mapMatrix, portals, this::notAccessibleTiles));

        return validationMessages;
    }

    private boolean notAccessibleTiles(Map.Entry<String, Boolean> accessibilityByTargetName) {
        return !accessibilityByTargetName.getValue();
    }

    @Override
    protected String transporterType() {
        return "portal";
    }
}
