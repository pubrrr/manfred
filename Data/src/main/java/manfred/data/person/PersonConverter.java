package manfred.data.person;

import manfred.data.InvalidInputException;
import manfred.data.ObjectConverter;

public class PersonConverter implements ObjectConverter<PersonDto, LocatedPersonDtoBuilder> {

    @Override
    public LocatedPersonDtoBuilder convert(PersonDto personDto) throws InvalidInputException {
        return null;
    }
}
