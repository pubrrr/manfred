package manfred.data.infrastructure.person;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.PersonDto;

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
