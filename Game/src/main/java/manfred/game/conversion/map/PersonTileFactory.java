package manfred.game.conversion.map;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.Person;
import manfred.game.interact.person.gelaber.GelaberConverter;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class PersonTileFactory implements TileConversionRule {

    // TODO this should not need to be here
    private final GameConfig gameConfig;

    private final GelaberConverter gelaberConverter;

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        return input.getPersons().stream()
            .filter(personPrototype -> personPrototype.getPositionX() == x && personPrototype.getPositionX() == y)
            .findFirst()
            .map(personPrototype -> new TileFromDtoAction<>(personPrototype, personFactory()));
    }

    private Function<PersonPrototype, MapTile> personFactory() {
        return personPrototype -> new Person(
            personPrototype.getName(),
            gelaberConverter.convert(personPrototype.getGelaber()),
            gameConfig,
            personPrototype.getImage()
        );
    }
}
