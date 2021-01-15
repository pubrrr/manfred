package manfred.game.interact.person;

import manfred.data.ObjectProvider;
import manfred.data.person.PersonDto;
import manfred.data.person.PersonReader;
import org.springframework.stereotype.Component;

@Component
public class PersonProvider extends ObjectProvider<PersonDto, Person> {

    protected PersonProvider(PersonReader objectReader, PersonConverter objectConverter) {
        super(objectReader, objectConverter);
    }
}
