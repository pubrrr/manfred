package manfred.infrastructureadapter.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.Person;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class PersonTileFactory implements TileConversionRule<MapTile> {

    // TODO this should not need to be here
    private final GameConfig gameConfig;

    private final GelaberConverter gelaberConverter;

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return input.getPerson(coordinate)
            .map(personPrototype -> new TileFromDtoAction<>(personPrototype, personFactory()));
    }

    private Function<PersonPrototype, MapTile> personFactory() {
        return personPrototype -> new Person(
            personPrototype.getName(),
            gelaberConverter.convert(personPrototype.getGelaber()),
            new SimpleSprite(gameConfig.getPixelBlockSize(), gameConfig.getPixelBlockSize(), personPrototype.getImage())
        );
    }
}
