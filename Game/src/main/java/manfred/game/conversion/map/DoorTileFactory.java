package manfred.game.conversion.map;

import manfred.data.map.TransporterDto;
import manfred.data.map.ValidatedMapDto;
import manfred.game.interact.Door;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

public class DoorTileFactory extends SpecialTileFactory<TransporterDto> implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        return findDtoAt(input.getDoors(), x, y)
            .map(doorDto -> new TileFromDtoAction<>(doorDto, doorFactory()));
    }

    private Function<TransporterDto, MapTile> doorFactory() {
        return doorDto -> new Door(doorDto.getTarget(), doorDto.getTargetSpawnX(), doorDto.getTargetSpawnY());
    }
}
