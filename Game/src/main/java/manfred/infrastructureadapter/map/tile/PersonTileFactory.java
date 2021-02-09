package manfred.infrastructureadapter.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.Person;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class PersonTileFactory implements TileConversionRule {

    // TODO this should not need to be here
    private final GameConfig gameConfig;

    private final GelaberConverter gelaberConverter;

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        return input.getPersons().stream()
            .filter(personPrototype -> personPrototype.getPositionX().value() == x && personPrototype.getPositionY().value() == y)
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
