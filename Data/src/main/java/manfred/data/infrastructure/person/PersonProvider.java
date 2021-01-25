package manfred.data.infrastructure.person;

import manfred.data.ObjectProvider;
import manfred.data.persistence.dto.PersonDto;
import manfred.data.persistence.reader.PersonReader;
import org.springframework.stereotype.Component;

@Component
public class PersonProvider extends ObjectProvider<PersonDto, PersonPrototypeBuilder> {

    public PersonProvider(PersonReader personReader, PersonConverter personConverter) {
        super(personReader, personConverter);
    }
}
