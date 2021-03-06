package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.interact.Portal;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

public class PortalTileFactory implements TileConversionRule<MapTile> {

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return input.getPortal(coordinate)
            .map(portalDto -> new TileFromDtoAction<>(portalDto, portalFactory()));
    }

    private Function<TransporterDto, MapTile> portalFactory() {
        return portalDto -> new Portal(portalDto.getTarget(), portalDto.getTargetSpawnX(), portalDto.getTargetSpawnY());
    }
}
