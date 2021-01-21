package manfred.data.person;

import manfred.data.ObjectProvider;

public class PersonProvider extends ObjectProvider<PersonDto, LocatedPersonDtoBuilder> {

    public PersonProvider(PersonReader personReader, PersonConverter personConverter) {
        super(personReader, personConverter);
    }
}
