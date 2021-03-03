package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.interact.Door;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

public class DoorTileFactory implements TileConversionRule<MapTile> {

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return input.getDoor(coordinate)
            .map(doorDto -> new TileFromDtoAction<>(doorDto, doorFactory()));
    }

    private Function<TransporterDto, MapTile> doorFactory() {
        return doorDto -> new Door(doorDto.getTarget(), doorDto.getTargetSpawnX(), doorDto.getTargetSpawnY());
    }
}
