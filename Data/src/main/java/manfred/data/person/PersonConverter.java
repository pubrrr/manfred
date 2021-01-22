package manfred.data.person;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.ObjectConverter;
import manfred.data.person.gelaber.GelaberValidator;

@AllArgsConstructor
public class PersonConverter implements ObjectConverter<PersonDto, LocatedPersonDtoBuilder> {

    private final GelaberValidator gelaberValidator;

    @Override
    public LocatedPersonDtoBuilder convert(PersonDto personDto) throws InvalidInputException {
        return new LocatedPersonDtoBuilder(
            personDto.getName(),
            gelaberValidator.validate(personDto.getGelaber()),
            personDto.getImage()
        );
    }
}
