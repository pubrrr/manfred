package manfred.data.map.validator;

import manfred.data.map.RawMapDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.util.List;

public class PersonsValidator implements Validator {

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        return List.of();
    }
}
