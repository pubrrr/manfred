package manfred.game.interact.person;

import manfred.data.InvalidInputException;
import manfred.data.ObjectConverter;
import manfred.data.person.PersonDto;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.GelaberConverter;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter implements ObjectConverter<PersonDto, Person> {

    private final GelaberConverter gelaberConverter;
    private final GameConfig gameConfig;

    public PersonConverter(GelaberConverter gelaberConverter, GameConfig gameConfig) {
        this.gelaberConverter = gelaberConverter;
        this.gameConfig = gameConfig;
    }

    public Person convert(PersonDto personDto) throws InvalidInputException {
        return new Person(
            personDto.getName(),
            gelaberConverter.convert(personDto.getGelaber()),
            gameConfig,
            personDto.getImage()
        );
    }
}
