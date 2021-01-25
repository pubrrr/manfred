package manfred.data.infrastructure.person;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.PersonDto;

@AllArgsConstructor
public class PersonConverter implements ObjectConverter<PersonDto, PersonPrototypeBuilder> {

    private final GelaberValidator gelaberValidator;

    @Override
    public PersonPrototypeBuilder convert(PersonDto personDto) throws InvalidInputException {
        GelaberPrototype gelaberPrototype;
        try {
            gelaberPrototype = gelaberValidator.validate(personDto.getGelaber());
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Error when validating gelaber for person " + personDto.getName(), e);
        }
        return new PersonPrototypeBuilder(
            personDto.getName(),
            gelaberPrototype,
            personDto.getImage()
        );
    }
}
