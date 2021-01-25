package manfred.data.infrastructure.map.validator;

import manfred.data.persistence.dto.RawMapDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;

import java.util.List;

public interface Validator {

    List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix);
}
