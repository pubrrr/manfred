package manfred.game.conversion.map;

import manfred.data.infrastructure.map.ValidatedMapDto;

import java.util.Optional;

public class PersonTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        return Optional.empty();
//        return input.getPersons().stream()
//            .filter(personDto -> personDto.getPositionX() == x && personDto.getPositionX() == y)
//            .findFirst()
//            .map(personDto -> new TileFromDtoAction<>(personDto, personDto1 -> new Person(personDto1.getName(), , gameConfig, personDto1.get)));
    }
}
