package manfred.data.map.validator;

import manfred.data.map.RawMapDto;

import java.util.List;

public interface Validator {

    List<String> validate(RawMapDto rawMapDto);
}
