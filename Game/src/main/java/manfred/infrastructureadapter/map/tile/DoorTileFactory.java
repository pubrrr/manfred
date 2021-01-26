package manfred.infrastructureadapter.map.tile;

import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.interact.Door;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

public class DoorTileFactory extends SpecialTileFactory<TransporterDto> implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        return findDtoAt(input.getDoors(), x, y)
            .map(doorDto -> new TileFromDtoAction<>(doorDto, doorFactory()));
    }

    private Function<TransporterDto, MapTile> doorFactory() {
        return doorDto -> new Door(doorDto.getTarget(), doorDto.getTargetSpawnX(), doorDto.getTargetSpawnY());
    }
}
