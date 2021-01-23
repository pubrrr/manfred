package manfred.data.infrastructure.person;

import manfred.data.ObjectProvider;
import manfred.data.persistence.dto.PersonDto;
import manfred.data.persistence.reader.PersonReader;

public class PersonProvider extends ObjectProvider<PersonDto, LocatedPersonDtoBuilder> {

    public PersonProvider(PersonReader personReader, PersonConverter personConverter) {
        super(personReader, personConverter);
    }
}
