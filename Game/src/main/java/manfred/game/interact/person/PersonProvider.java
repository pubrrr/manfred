package manfred.game.interact.person;

import manfred.data.ObjectProvider;
import manfred.data.persistence.dto.PersonDto;
import manfred.data.persistence.reader.PersonReader;
import org.springframework.stereotype.Component;

@Component
public class PersonProvider extends ObjectProvider<PersonDto, Person> {

    protected PersonProvider(PersonReader objectReader, PersonConverter objectConverter) {
        super(objectReader, objectConverter);
    }
}
