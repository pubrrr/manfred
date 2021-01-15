package manfred.data.map.validator;

import manfred.data.map.RawMapDto;

import java.util.List;

public class PersonsValidator implements Validator {

    @Override
    public List<String> validate(RawMapDto rawMapDto) {
        return List.of();
    }
}
